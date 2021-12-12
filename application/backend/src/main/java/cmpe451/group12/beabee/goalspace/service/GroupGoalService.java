package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.common.util.UUIDShortener;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.*;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsGetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupGoalService
{
    private final GroupGoalRepository groupGoalRepository;
    private final SubgoalRepository subgoalRepository;
    private final GroupGoalPostMapper groupGoalPostMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final GroupGoalGetMapper groupGoalGetMapper;
    private final GroupGoalShortMapper groupGoalShortMapper;
    private final UserRepository userRepository;
    private final EntitiShortMapper entitiShortMapper;
    private final UserCredentialsGetMapper userCredentialsGetMapper;


    private Set<EntitiDTOShort> extractEntities(GroupGoal groupGoal){

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    public GroupGoalGetDto getAGroupgoal(Long groupgoal_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );
        GroupGoalGetDto groupGoalGetDto = groupGoalGetMapper.mapToDto(groupGoal);
        groupGoalGetDto.setUser_id(groupGoal.getCreator().getUser_id());
        groupGoalGetDto.setSubgoals(new HashSet<>(subgoalShortMapper.mapToDto(new ArrayList<>(groupGoal.getSubgoals()))));
        groupGoalGetDto.setEntities(extractEntities(groupGoal));
        //groupGoalGetDto.setMembers(userCredentialsGetMapper.mapToDto(groupGoal.getMembers().stream().map(x->{x.setPassword("***"); return x;}).collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        return groupGoalGetDto;
    }

    public List<GroupGoalDTOShort> getGroupgoalsCreatedByAUser(Long user_id) {
        return groupGoalShortMapper.mapToDto(groupGoalRepository.findAllByUserId(user_id));
    }

    public List<GroupGoalDTOShort> getGroupgoalsOfAUser(Long user_id) {
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        return groupGoalShortMapper.mapToDto(new ArrayList<>(user.getMemberOf()));
    }

    public MessageResponse regenerateToken(Long groupgoal_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );

        groupGoal.setToken(UUIDShortener.randomShortUUID());
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("Group goal token regenerated.", MessageType.SUCCESS);
    }

    public MessageResponse updateAGroupgoal(GroupGoalGetDto groupGoalGetDto) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupGoalGetDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );

        if (groupGoalGetDto.getTitle() != null){
            groupGoal.setTitle(groupGoalGetDto.getTitle());
        }
        if (groupGoalGetDto.getDescription() != null){
            groupGoal.setDescription(groupGoalGetDto.getDescription());
        }
        if (groupGoalGetDto.getDeadline() != null){
            groupGoal.setDeadline(groupGoalGetDto.getDeadline());
        }
        if (groupGoalGetDto.getIsDone() != null){
            groupGoal.setIsDone(groupGoalGetDto.getIsDone());
        }
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("Group Goal updated!", MessageType.SUCCESS);
    }


    public MessageResponse createAGroupgoal(Long user_id, GroupGoalPostDTO groupGoalPostDTO)
    {
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        GroupGoal new_groupgoal = groupGoalPostMapper.mapToEntity(groupGoalPostDTO);
        new_groupgoal.setIsDone(Boolean.FALSE);
        new_groupgoal.setCreator(user);

        //Do we still need this? -nah. doesn't matter whether we keep it
        new_groupgoal.setGoalType(GoalType.GROUPGOAL);

        //Use UUID converted to URL62 Base to guarantee uniqueness and improve readability
        new_groupgoal.setToken(UUIDShortener.randomShortUUID());

        //Add creator to the member list
        HashSet<Users> members = new HashSet<>();
        members.add(user);
        new_groupgoal.setMembers(members);

        groupGoalRepository.save(new_groupgoal);
        return new MessageResponse("Group Goal added successfully.", MessageType.SUCCESS);
    }

    public MessageResponse deleteGroupgoal(Long groupgoal_id)
    {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!"));
        for(Users user : groupGoal.getMembers()) {
            Optional<Users> userOptional = userRepository.findById(user.getUser_id());
            if(userOptional.isPresent()) {
                user.getMemberOf().remove(groupGoal);
            }
            userRepository.save(user);
        }
        groupGoalRepository.deleteById(groupGoal.getId());
        return new MessageResponse("Group goal deleted.", MessageType.SUCCESS);
    }

    public MessageResponse createSubgoal(SubgoalPostDTO subgoal_dto)
    {
        GroupGoal groupGoal = groupGoalRepository.findById(subgoal_dto.getMain_groupgoal_id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!"));
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoal_dto);
        new_subgoal.setMainGroupgoal(groupGoal);
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setCreator(groupGoal.getCreator());
        new_subgoal.setExtension_count(0L);
        new_subgoal.setRating(0D);
        subgoalRepository.save(new_subgoal);

        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    public GroupGoalDTOShort joinWithToken(Long user_id, String token)
    {
        GroupGoal groupGoal = groupGoalRepository.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not valid!")
        );

        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!")
        );

        groupGoal.getMembers().add(user);

        groupGoalRepository.save(groupGoal);

        return groupGoalShortMapper.mapToDto(groupGoal);
    }

    public MessageResponse addMember(Long groupgoal_id, String username) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal does not exist")
        );
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );

        groupGoal.getMembers().add(user);

        groupGoalRepository.save(groupGoal);

        return new MessageResponse("User added to group successfully!", MessageType.SUCCESS);
    }

    public MessageResponse leaveGroupGoal(Long groupgoal_id, Long user_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal does not exist")
        );
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );

        if(Objects.equals(groupGoal.getCreator().getUser_id(), user.getUser_id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot leave a group goal they created!");
        }

        groupGoal.getMembers().remove(user);

        groupGoalRepository.save(groupGoal);
        return new MessageResponse("User left group goal successfully!", MessageType.SUCCESS);
    }
}
