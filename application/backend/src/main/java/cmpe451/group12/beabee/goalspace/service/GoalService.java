package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.goalspace.dto.GoalDTO;
import cmpe451.group12.beabee.goalspace.mapper.GoalMapper;
import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Goal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;
    private final UserRepository userRepository;
    public GoalDTO getAGoal(Long goal_id) {
        return  goalMapper.mapToDto(goalRepository.findById(goal_id).orElse(new Goal()));
    }

    public List<GoalDTO> getGoalsOfAUser(Long user_id) {
        return goalMapper.mapToDto(goalRepository.findAllByUserId(user_id));
    }

    public MessageResponse updateAGoal(GoalDTO goalDTO){
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goalDTO.getId());
        if (goal_from_db_opt.isEmpty()){
            return new MessageResponse("Goal does not exists. So, I cannot update it.", MessageType.ERROR);
        }
        Goal goal_from_db = goal_from_db_opt.get();
        if (goalDTO.getTitle() != null){
            goal_from_db.setTitle(goalDTO.getTitle());
        }
        if (goalDTO.getDescription() != null){
            goal_from_db.setDescription(goalDTO.getDescription());
        }
        if (goalDTO.getDeadline() != null){
            goal_from_db.setDeadline(goalDTO.getDeadline());
        }
        if (goalDTO.getGoalType() != null){
            goal_from_db.setGoalType(goalDTO.getGoalType());
        }

        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal updated!", MessageType.SUCCESS);
    }

    public MessageResponse createAGoal(Long user_id, GoalDTO goalDto) {
        Optional<Users> user = userRepository.findById(user_id);
        if (user.isEmpty()){
            return new MessageResponse("User does not exists!", MessageType.ERROR);
        }
        Goal new_goal = goalMapper.mapToEntity(goalDto);
        new_goal.setIsDone(Boolean.FALSE);
        new_goal.setCreator(user.get());
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
            return new MessageResponse("Goal does not exists!", MessageType.ERROR);
        }
        goalRepository.delete(goal_from_db_opt.get());
        return new MessageResponse("Goal deleted.", MessageType.SUCCESS);
    }
}
