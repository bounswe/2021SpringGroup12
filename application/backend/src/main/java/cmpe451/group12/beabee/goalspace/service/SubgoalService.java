package cmpe451.group12.beabee.goalspace.service;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.EntitiRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.SubgoalAnalyticsDTO;
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
import java.util.stream.Stream;

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
        new_subgoal.setExtension_count(0L);
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

    private Set<EntitiDTOShort> extractEntities(Subgoal subgoal) {

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
        } else if (subgoal_from_db_opt.get().getMainGroupgoal() != null) {
            subgoalGetDTO.setMain_goal_id(subgoal_from_db_opt.get().getMainGroupgoal().getId());
        } else {
            subgoalGetDTO.setParent_subgoal_id(subgoalRepository.findParentById(id).getId());
        }
        subgoalGetDTO.setSublinks(new HashSet<>(subgoalShortMapper.mapToDto(new ArrayList<>(subgoal_from_db_opt.get().getChild_subgoals()))));
        subgoalGetDTO.setEntities(extractEntities(subgoal_from_db_opt.get()));
        subgoalGetDTO.setAssignees(subgoal_from_db_opt.get().getAssignees().stream().map(Users::getUser_id).collect(Collectors.toSet()));
        return subgoalGetDTO;
    }

    /********************* EXTEND AND COMPLETE start *************/
    public MessageResponse extendSubgoal(Long subgoal_id, Date newDeadline) {
        Subgoal subgoal_from_db = subgoalRepository.findById(subgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!"));
        if (newDeadline.compareTo(subgoal_from_db.getDeadline()) <= 0) {
            return new MessageResponse("New deadline must be later than current deadline!", MessageType.ERROR);
        }
        subgoal_from_db.setDeadline(newDeadline);
        subgoal_from_db.setExtension_count(subgoal_from_db.getExtension_count() + 1);
        subgoalRepository.save(subgoal_from_db);
        return new MessageResponse("Subgoal extended successfully!", MessageType.SUCCESS);
    }

    public MessageResponse completeSubgoal(Long subgoal_id, Long rating) {
        Subgoal subgoal_from_db = subgoalRepository.findById(subgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!"));
        if (subgoal_from_db.getIsDone()) {
            return new MessageResponse("Already completed!", MessageType.ERROR);
        }
        if (subgoal_from_db.getChild_subgoals().stream().filter(x -> !x.getIsDone()).count() > 0) {
            return new MessageResponse("This subgoal has some subgoals that are uncompleted! Finish those first!", MessageType.ERROR);
        }
        subgoal_from_db.setIsDone(Boolean.TRUE);
        subgoal_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        if (subgoal_from_db.getChild_subgoals().size() > 0) {
            subgoal_from_db.setRating((rating.doubleValue() + subgoal_from_db.getChild_subgoals().stream().map(x -> x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage()) / 2);
        } else {
            subgoal_from_db.setRating(rating.doubleValue());

        }
        /*
        if(subgoal_from_db.getChild_subgoals().size() == 0){
            // it has not child subgoals
            subgoal_from_db.setRating(rating.doubleValue());
        }else {
            // it has child subgoals
            // NOTE : this code is GEM!
            // Gets all children and grandchildren and grandchildren ...
            //
            List<Subgoal> children = subgoal_from_db.getChild_subgoals().stream()
                    .flatMap(SubgoalService::flatMapRecursive).collect(Collectors.toList());
            // set completed but not rated children's rating
            children.stream().forEach(x->{x.setCompletedAt(new Date(System.currentTimeMillis())); x.setIsDone(Boolean.TRUE);});
            children.stream().filter(x-> x.getRating() == 0).forEach(x->x.setRating(rating.doubleValue()));
            // calculate current rating as average of its children
            //System.out.println(children.stream().map(x->{return x.getId() + " "+ x.getRating();}).collect(Collectors.toList()));
            subgoal_from_db.setRating(children.stream().map(x -> x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
            subgoalRepository.saveAll(children);
        }*/
        subgoalRepository.save(subgoal_from_db);
        return new MessageResponse("Subgoal completed successfully!", MessageType.SUCCESS);
    }

    private static Stream<Subgoal> flatMapRecursive(Subgoal item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(SubgoalService::flatMapRecursive));
    }

    /********************* EXTEND AND COMPLETE finish *************/
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
            for (Users user : subgoal_opt.get().getAssignees()) {
                user.getAssigned().remove(subgoal_opt.get());
                userRepository.save(user);
            }
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } else if (subgoal_opt.get().getMainGroupgoal() != null) {
            GroupGoal main_groupgoal = subgoal_opt.get().getMainGroupgoal();
            Set<Subgoal> subgoals = main_groupgoal.getSubgoals();
            subgoals.remove(subgoal_opt.get());
            main_groupgoal.setSubgoals(subgoals);
            groupGoalRepository.save(main_groupgoal);
            for (Users user : subgoal_opt.get().getAssignees()) {
                user.getAssigned().remove(subgoal_opt.get());
                userRepository.save(user);
            }
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } else {
            Subgoal parent_subgoal = subgoalRepository.findParentById(id);
            Set<Subgoal> child_subgoals = parent_subgoal.getChild_subgoals();
            child_subgoals.remove(subgoal_opt.get());
            parent_subgoal.setChild_subgoals(child_subgoals);
            subgoalRepository.save(parent_subgoal);
            for (Users user : subgoal_opt.get().getAssignees()) {
                user.getAssigned().remove(subgoal_opt.get());
                userRepository.save(user);
            }
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

    /****************** EXXTEND - COMPLETE ************/


    public MessageResponse addAssignees(long subgoal_id, List<Long> user_ids) {
        Subgoal subgoal = subgoalRepository.findById(subgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!")
        );
        if (subgoal.getMainGroupgoal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subgoal is not linked to a group goal!");
        }
        for (Long user_id : user_ids) {
            Users user = userRepository.findById(user_id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id:" + user_id + " not found!")
            );
            if (!subgoal.getMainGroupgoal().getMembers().contains(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id:" + user_id + " not found!");
            }
            if (!subgoal.getAssignees().contains(user)) {
                subgoal.getAssignees().add(user);
                ;
            }
        }

        subgoalRepository.save(subgoal);

        return new MessageResponse("Assigned to users successfully!", MessageType.SUCCESS);
    }

    public MessageResponse removeAssignees(long subgoal_id, List<Long> user_ids) {
        Subgoal subgoal = subgoalRepository.findById(subgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!")
        );
        if (subgoal.getMainGroupgoal() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Subgoal is not linked to a group goal!");
        }
        for (Long user_id : user_ids) {
            Users user = userRepository.findById(user_id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id:" + user_id + " not found!")
            );
            if (!subgoal.getMainGroupgoal().getMembers().contains(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id:" + user_id + " is not a member of the group goal!");
            }
            if (!subgoal.getAssignees().contains(user)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id:" + user_id + " is not an assignee of the subgoal!");
            }
            user.getAssigned().remove(subgoal);
            userRepository.save(user);
            subgoal.getAssignees().remove(user);
        }

        subgoalRepository.save(subgoal);

        return new MessageResponse("Assignment revoked from users successfully!", MessageType.SUCCESS);
    }

    public SubgoalAnalyticsDTO getAnalytics(Long subgoal_id) {
        Subgoal subgoal_from_db = subgoalRepository.findById(subgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!"));
        SubgoalAnalyticsDTO subgoalAnalyticsDTO = new SubgoalAnalyticsDTO();

        subgoalAnalyticsDTO.setSubgoal_id(subgoal_id);
        subgoalAnalyticsDTO.setExtensionCount(subgoal_from_db.getExtension_count());
        subgoalAnalyticsDTO.setStartTime(subgoal_from_db.getCreatedAt());
        if (subgoal_from_db.getIsDone()) {
            subgoalAnalyticsDTO.setStatus(SubgoalAnalyticsDTO.Status.COMPLETED);
            subgoalAnalyticsDTO.setRating(subgoal_from_db.getRating());
            subgoalAnalyticsDTO.setFinishTime(subgoal_from_db.getCompletedAt());
            subgoalAnalyticsDTO.setCompletionTimeInMiliseconds(subgoal_from_db.getCompletedAt().getTime() - subgoal_from_db.getCreatedAt().getTime());
        } else {
            subgoalAnalyticsDTO.setRating(subgoal_from_db.getChild_subgoals().stream().filter(z -> z.getIsDone()).map(x ->
                    x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
            subgoalAnalyticsDTO.setStatus(SubgoalAnalyticsDTO.Status.ACTIVE);
        }
        if (subgoal_from_db.getChild_subgoals().size() > 0) {

            subgoalAnalyticsDTO.setLongestSubgoal(subgoalShortMapper.mapToDto(subgoal_from_db.getChild_subgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get()));
            subgoalAnalyticsDTO.setShortestSubgoal(subgoalShortMapper.mapToDto(subgoal_from_db.getChild_subgoals().stream().filter(z -> z.getIsDone()).min(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get()));
            subgoalAnalyticsDTO.setBestSubgoal(subgoalShortMapper.mapToDto(subgoal_from_db.getChild_subgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(Subgoal::getRating)).get()));
            subgoalAnalyticsDTO.setWorstSubgoal(subgoalShortMapper.mapToDto(subgoal_from_db.getChild_subgoals().stream().filter(z -> z.getIsDone()).min(Comparator.comparing(Subgoal::getRating)).get()));

            subgoalAnalyticsDTO.setAverageCompletionTimeOfSubgoals((long) subgoal_from_db.getChild_subgoals().stream().filter(x -> x.getCompletedAt() != null).map(x ->
                    x.getCompletedAt().getTime() - x.getCreatedAt().getTime()).mapToLong(Long::longValue).summaryStatistics().getAverage());
        }
        subgoalAnalyticsDTO.setActiveSubgoalCount(subgoal_from_db.getChild_subgoals().stream().filter(x->!x.getIsDone()).count());
        subgoalAnalyticsDTO.setCompletedSubgoalCount(subgoal_from_db.getChild_subgoals().stream().filter(x->x.getIsDone()).count());

        return subgoalAnalyticsDTO;

    }
}
