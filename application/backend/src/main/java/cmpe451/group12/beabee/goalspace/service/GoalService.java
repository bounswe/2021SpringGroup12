package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalPostDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public GoalGetDTO getAGoal(Long goal_id) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goal_id);
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        GoalGetDTO goalGetDTO = goalGetMapper.mapToDto(goal_from_db_opt.get());
        goalGetDTO.setUser_id(goal_from_db_opt.get().getCreator().getUser_id());
        goalGetDTO.setSubgoals(subgoalShortMapper.mapToDto(goal_from_db_opt.get().getSubgoals().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        return  goalGetDTO;
    }

    public List<GoalDTOShort> getGoalsOfAUser(Long user_id) {
        return goalShortMapper.mapToDto(goalRepository.findAllByUserId(user_id));
    }

    public MessageResponse updateAGoal(GoalGetDTO goalGetDTO){
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goalGetDTO.getId());
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        Goal goal_from_db = goal_from_db_opt.get();
        if (goalGetDTO.getTitle() != null){
            goal_from_db.setTitle(goalGetDTO.getTitle());
        }
        if (goalGetDTO.getDescription() != null){
            goal_from_db.setDescription(goalGetDTO.getDescription());
        }
        if (goalGetDTO.getDeadline() != null){
            goal_from_db.setDeadline(goalGetDTO.getDeadline());
        }
        if (goalGetDTO.getIsDone() != null){
            goal_from_db.setIsDone(goalGetDTO.getIsDone());
        }
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal updated!", MessageType.SUCCESS);
    }

    public MessageResponse createAGoal(Long user_id, GoalPostDTO goalPostDTO) {
        Optional<Users> user = userRepository.findById(user_id);
        if (user.isEmpty()){
            return new MessageResponse("User does not exists!", MessageType.ERROR);
        }
        Goal new_goal = goalPostMapper.mapToEntity(goalPostDTO);
        new_goal.setIsDone(Boolean.FALSE);
        new_goal.setCreator(user.get());
        new_goal.setGoalType(GoalType.GOAL);
        goalRepository.save(new_goal);
        return new MessageResponse("Goal added successfully.", MessageType.SUCCESS);
    }

    /**
     * When a goal is deleted all of its entities are also deleted.
     * @param goal_id
     * @return
     */
    public MessageResponse deleteGoal(Long goal_id) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goal_id);
        if (goal_from_db_opt.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        goalRepository.deleteAGoal(goal_from_db_opt.get().getId());
        return new MessageResponse("Goal deleted.", MessageType.SUCCESS);
    }

    /************ SUBGOAL CREATION ******/
    public MessageResponse createSubgoal(SubgoalPostDTO subgoalPostDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(subgoalPostDTO.getMain_goal_id());
        if (goal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoalPostDTO);
        new_subgoal.setMainGoal(goal_opt.get());
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setRating(0D);
        subgoalRepository.save(new_subgoal);
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

}
