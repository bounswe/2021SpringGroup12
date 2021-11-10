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

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        return goalMapper.mapToDto(goalRepository.findAllByCreatorId(user_id));
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

/*    public MessageResponse createEntity(Long id, Long goal_id, EntityDTO entityDto) {
        Users user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Goal goal = goalRepository.findById(goal_id).orElseThrow(EntityNotFoundException::new);
        Set<Entity> goal_entities = goal.getEntities();
        goal_entities.add(entityMapper.mapToEntity(entityDto));
        goal.setEntities(goal_entities);
        return new MessageResponse("entity added", MessageType.SUCCESS);
    }
 */
/*
    public List<Entiti> getEntities(Long id){
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new).getEntities().stream().map(x -> entityMapper.mapToDto(x)).collect(Collectors.toList());
    }
*/

    public MessageResponse createAGoal(Long user_id, GoalDTO goalDto) {
        Optional<Users> user = userRepository.findById(user_id);
        if (user.isEmpty()){
            return new MessageResponse("User does not exists!", MessageType.ERROR);
        }
        Set<Goal> user_goals = user.get().getGoals();



        Goal new_goal = goalMapper.mapToEntity(goalDto);
        // TODO: set null fields
        new_goal.setIsDone(Boolean.FALSE);
        user_goals.add(new_goal);
        user.get().setGoals(user_goals);
        new_goal.setCreator(user.get());
        goalRepository.save(new_goal);
        userRepository.save(user.get());
        return new MessageResponse("Goal added successfully.", MessageType.SUCCESS);
    }

}
