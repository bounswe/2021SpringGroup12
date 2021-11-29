package cmpe451.group12.beabee.goalspace.service;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.EntitiRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiMapper;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalPostMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubgoalService {

    private final SubgoalGetMapper subgoalGetMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalRepository subgoalRepository;
    private final GoalRepository goalRepository;
    private final GroupGoalRepository groupGoalRepository;
    private final UserRepository userRepository;
    private final EntitiMapper entitiMapper;
    private final EntitiRepository entitiRepository;
    private final EntitiShortMapper entitiShortMapper;


    public MessageResponse createSubgoal(SubgoalPostDTO subgoalPostDTO) {
        Optional<Subgoal> parent_subgoal_opt = subgoalRepository.findById(subgoalPostDTO.getParent_subgoal_id());
        if (parent_subgoal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
        }
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoalPostDTO);

        // initialize fields of new subgoal
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setRating(0D);
        // Add this subgoal to parent subgoal
        Set<Subgoal> subgoals_of_parent = parent_subgoal_opt.get().getChild_subgoals();
        subgoals_of_parent.add(new_subgoal);
        parent_subgoal_opt.get().setChild_subgoals(subgoals_of_parent);
        new_subgoal.setCreator(parent_subgoal_opt.get().getCreator());
        // save to DB
        subgoalRepository.save(new_subgoal);
        subgoalRepository.save(parent_subgoal_opt.get());
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    private Set<EntitiDTOShort> extractEntities(Subgoal subgoal){

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                subgoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                subgoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                subgoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                subgoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    public SubgoalGetDTO getSubgoal(Long id) {
        Optional<Subgoal> subgoal_from_db_opt = subgoalRepository.findById(id);
        if (subgoal_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        SubgoalGetDTO subgoalGetDTO = subgoalGetMapper.mapToDto(subgoal_from_db_opt.get());
        if (subgoal_from_db_opt.get().getMainGoal() != null) {
            subgoalGetDTO.setMain_goal_id(subgoal_from_db_opt.get().getMainGoal().getId());
        }else{
            subgoalGetDTO.setParent_subgoal_id(subgoalRepository.findParentById(id).getId());
        }
        subgoalGetDTO.setSublinks(subgoalShortMapper.mapToDto(subgoal_from_db_opt.get().getChild_subgoals().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        subgoalGetDTO.setEntities(extractEntities(subgoal_from_db_opt.get()));
        return subgoalGetDTO;
    }


    // I wrote this but I don't know where it will be used.
    public List<SubgoalGetDTO> getSubgoalsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        return subgoalGetMapper.mapToDto(subgoalRepository.findAllByCreator(userRepository.getById(user_id)));
    }

    public List<SubgoalDTOShort> getSubgoalsOfAGoal(Long goal_id) {
        if (!goalRepository.existsById(goal_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        return subgoalShortMapper.mapToDto(subgoalRepository.findByGoalId(goal_id));
    }

    public List<SubgoalDTOShort> getSubgoalsOfASubgoal(Long subgoal_id) {
        if (!subgoalRepository.existsById(subgoal_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        return subgoalShortMapper.mapToDto(subgoalRepository.findById(subgoal_id).get().getChild_subgoals().stream().collect(Collectors.toList()));
    }

    // Works great!
    public MessageResponse deleteSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        if (subgoal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        if (subgoal_opt.get().getMainGoal() != null) {
            //level 1 subgoal
            Goal main_goal = subgoal_opt.get().getMainGoal();
            Set<Subgoal> subgoals = main_goal.getSubgoals();
            subgoals.remove(subgoal_opt.get());
            main_goal.setSubgoals(subgoals);
            goalRepository.save(main_goal);
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } else if (subgoal_opt.get().getMainGroupgoal() != null) {
            GroupGoal main_groupgoal = subgoal_opt.get().getMainGroupgoal();
            Set<Subgoal> subgoals = main_groupgoal.getSubgoals();
            subgoals.remove(subgoal_opt.get());
            main_groupgoal.setSubgoals(subgoals);
            groupGoalRepository.save(main_groupgoal);
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } else  {
            Subgoal parent_subgoal = subgoalRepository.findParentById(id);
            Set<Subgoal> child_subgoals = parent_subgoal.getChild_subgoals();
            child_subgoals.remove(subgoal_opt.get());
            parent_subgoal.setChild_subgoals(child_subgoals);
            subgoalRepository.save(parent_subgoal);
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        }
    }

    public MessageResponse updateSubgoal(SubgoalGetDTO subgoal_dto) {
        Optional<Subgoal> subgoal_from_db_opt = subgoalRepository.findById(subgoal_dto.getId());
        if (subgoal_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        if (subgoal_dto.getRating() != null)
            subgoal_from_db_opt.get().setRating(subgoal_dto.getRating());
        if (subgoal_dto.getDeadline() != null)
            subgoal_from_db_opt.get().setDeadline(subgoal_dto.getDeadline());
        if (subgoal_dto.getDescription() != null)
            subgoal_from_db_opt.get().setDescription(subgoal_dto.getDescription());
        if (subgoal_dto.getTitle() != null)
            subgoal_from_db_opt.get().setTitle(subgoal_dto.getTitle());
        if (subgoal_dto.getIsDone() != null)
            subgoal_from_db_opt.get().setIsDone(subgoal_dto.getIsDone());

        subgoalRepository.save(subgoal_from_db_opt.get());
        return new MessageResponse("Updated subgoal", MessageType.SUCCESS);
    }

}
