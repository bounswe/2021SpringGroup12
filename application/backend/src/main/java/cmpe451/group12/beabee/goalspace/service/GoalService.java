package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalPostDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
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
public class GoalService {

    private final GoalRepository goalRepository;
    private final SubgoalRepository subgoalRepository;
    private final GoalPostMapper goalPostMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final GoalGetMapper goalGetMapper;
    private final GoalShortMapper goalShortMapper;
    private final UserRepository userRepository;
    private final EntitiShortMapper entitiShortMapper;


    private Set<EntitiDTOShort> extractEntities(Goal goal) {

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    public GoalGetDTO getAGoal(Long goal_id) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goal_id);
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        GoalGetDTO goalGetDTO = goalGetMapper.mapToDto(goal_from_db_opt.get());
        goalGetDTO.setUser_id(goal_from_db_opt.get().getCreator().getUser_id());
        goalGetDTO.setSubgoals(subgoalShortMapper.mapToDto(goal_from_db_opt.get().getSubgoals().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        goalGetDTO.setEntities(extractEntities(goal_from_db_opt.get()));
        return goalGetDTO;
    }

    public List<GoalDTOShort> getGoalsOfAUser(Long user_id) {
        return goalShortMapper.mapToDto(goalRepository.findAllByUserId(user_id));
    }

    public MessageResponse updateAGoal(GoalGetDTO goalGetDTO) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goalGetDTO.getId());
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        Goal goal_from_db = goal_from_db_opt.get();
        if (goalGetDTO.getTitle() != null) {
            goal_from_db.setTitle(goalGetDTO.getTitle());
        }
        if (goalGetDTO.getDescription() != null) {
            goal_from_db.setDescription(goalGetDTO.getDescription());
        }
        if (goalGetDTO.getDeadline() != null) {
            goal_from_db.setDeadline(goalGetDTO.getDeadline());
        }
        if (goalGetDTO.getIsDone() != null) {
            goal_from_db.setIsDone(goalGetDTO.getIsDone());
        }
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal updated!", MessageType.SUCCESS);
    }

    public MessageResponse createAGoal(Long user_id, GoalPostDTO goalPostDTO) {
        Optional<Users> user = userRepository.findById(user_id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        Goal new_goal = goalPostMapper.mapToEntity(goalPostDTO);
        new_goal.setIsDone(Boolean.FALSE);
        new_goal.setCreator(user.get());
        new_goal.setExtension_count(0L);
        new_goal.setGoalType(GoalType.GOAL);
        new_goal.setRating(0D);
        goalRepository.save(new_goal);
        return new MessageResponse("Goal added successfully.", MessageType.SUCCESS);
    }

    /**
     * When a goal is deleted all of its entities are also deleted.
     *
     * @param goal_id
     * @return
     */
    public MessageResponse deleteGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        goalRepository.deleteAGoal(goal_from_db.getId());
        return new MessageResponse("Goal deleted.", MessageType.SUCCESS);
    }


    /********************* EXTEND AND COMPLETE start *************/
    public MessageResponse extendGoal(Long goal_id, Date newDeadline) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        if (newDeadline.compareTo(goal_from_db.getDeadline()) <= 0) {
            return new MessageResponse("New deadline must be later than current deadline!", MessageType.ERROR);
        }
        goal_from_db.setDeadline(newDeadline);
        goal_from_db.setExtension_count(goal_from_db.getExtension_count() + 1);
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal extended successfully!", MessageType.SUCCESS);
    }

    public MessageResponse completeGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        if (goal_from_db.getIsDone()) {
            return new MessageResponse("Already completed!", MessageType.ERROR);
        }
        goal_from_db.setIsDone(Boolean.TRUE);
        goal_from_db.setCompletedAt(new Date(System.currentTimeMillis()));

        List<Subgoal> subgoals = goal_from_db.getSubgoals().stream()
                .flatMap(GoalService::flatMapRecursive).collect(Collectors.toList());
        //System.out.println(subgoals.stream().map(x -> {return x.getId() +" "+x.getRating();}).collect(Collectors.toList()));

        subgoals.stream().forEach(x -> {
            x.setCompletedAt(new Date(System.currentTimeMillis()));
            x.setIsDone(Boolean.TRUE);
        });
        /* calculate current rating as average of its children, discard not rated children*/
        goal_from_db.setRating(subgoals.stream().filter(x -> x.getRating() > 0).map(x -> x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
        /* rate unrated children with the average*/
        subgoals.stream().filter(x -> x.getRating() == 0).forEach(x -> x.setRating(goal_from_db.getRating()));
        //System.out.println(children.stream().map(x->{return x.getId() + " "+ x.getRating();}).collect(Collectors.toList()));
        subgoalRepository.saveAll(subgoals);
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal completed successfully!", MessageType.SUCCESS);
    }

    private static Stream<Subgoal> flatMapRecursive(Subgoal item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(GoalService::flatMapRecursive));
    }
    /********************* EXTEND AND COMPLETE finish *************/

    /************ SUBGOAL CREATION ******/
    public MessageResponse createSubgoal(SubgoalPostDTO subgoalPostDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(subgoalPostDTO.getMain_goal_id());
        if (goal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoalPostDTO);
        new_subgoal.setMainGoal(goal_opt.get());
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setExtension_count(0L);
        new_subgoal.setCreator(goal_opt.get().getCreator());
        new_subgoal.setRating(0D);
        subgoalRepository.save(new_subgoal);
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    /********************* ANALYTICS **************/
    public GoalAnalyticsDTO getAnalytics(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        GoalAnalyticsDTO goalAnalyticsDTO = new GoalAnalyticsDTO();

        goalAnalyticsDTO.setGoal_id(goal_id);
        goalAnalyticsDTO.setExtensionCount(goal_from_db.getExtension_count());
        goalAnalyticsDTO.setRating(goal_from_db.getRating());
        goalAnalyticsDTO.setStartTime(goal_from_db.getCreatedAt());
        if (goal_from_db.getIsDone()) {
            goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.COMPLETED);
            goalAnalyticsDTO.setFinishTime(goal_from_db.getCompletedAt());
            goalAnalyticsDTO.setCompletionTimeInMiliseconds(goal_from_db.getCompletedAt().getTime() - goal_from_db.getCreatedAt().getTime());

            List<Goal> common_goals = goalRepository.findAllByCreatedAtIsBetweenOrCompletedAtBetween(goal_from_db.getCreatedAt(), goal_from_db.getCompletedAt(), goal_from_db.getCreatedAt(), goal_from_db.getCompletedAt()).stream().filter(x -> x.getCreator().equals(goal_from_db.getCreator())).collect(Collectors.toList());
            common_goals.remove(goal_from_db);
            goalAnalyticsDTO.setGoalsWithCommonLifetime(goalShortMapper.mapToDto(common_goals).stream().collect(Collectors.toSet()));

        } else {
            goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.ACTIVE);
            List<Goal> common_goals = goalRepository.findAllByCreatedAtIsBetweenOrCompletedAtBetween(goal_from_db.getCreatedAt(), new Date(System.currentTimeMillis()), goal_from_db.getCreatedAt(), new Date(System.currentTimeMillis())).stream().filter(x -> x.getCreator().equals(goal_from_db.getCreator())).collect(Collectors.toList());
            common_goals.remove(goal_from_db);
            goalAnalyticsDTO.setGoalsWithCommonLifetime(goalShortMapper.mapToDto(common_goals).stream().collect(Collectors.toSet()));
        }
        if (goal_from_db.getSubgoals().size() > 0) {
            //there is no subgoal, yet
            Set<Subgoal> subgoals_to_calculate_completion_time = goal_from_db.getSubgoals();
            subgoals_to_calculate_completion_time.stream().forEach(x -> {
                x.setCompletedAt(new Date(System.currentTimeMillis()));
            });

            goalAnalyticsDTO.setLongestSubgoal(subgoalShortMapper.mapToDto(subgoals_to_calculate_completion_time.stream().max(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get()));
            goalAnalyticsDTO.setShortestSubgoal(subgoalShortMapper.mapToDto(subgoals_to_calculate_completion_time.stream().min(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get()));
            goalAnalyticsDTO.setBestSubgoal(subgoalShortMapper.mapToDto(goal_from_db.getSubgoals().stream().max(Comparator.comparing(Subgoal::getRating)).get()));
            goalAnalyticsDTO.setWorstSubgoal(subgoalShortMapper.mapToDto(goal_from_db.getSubgoals().stream().min(Comparator.comparing(Subgoal::getRating)).get()));

            goalAnalyticsDTO.setAverageCompletionTimeOfSubgoals((long) goal_from_db.getSubgoals().stream().filter(x -> x.getCompletedAt() != null).map(x ->
                    x.getCompletedAt().getTime() - x.getCreatedAt().getTime()).mapToLong(Long::longValue).summaryStatistics().getAverage());
        }

        return goalAnalyticsDTO;
    }
}
