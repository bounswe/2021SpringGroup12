package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.goalspace.dto.GoalDTO;
import cmpe451.group12.beabee.goalspace.model.Goal;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/goals", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class GoalController {
    private final GoalService goalService;

    @GetMapping("/{goal_id}")
    public GoalDTO getGoal(@PathVariable Long goal_id) {
        return goalService.getAGoal(goal_id);
    }

    @GetMapping("/of_user/{user_id}")
    public List<GoalDTO> getGoalsOfAUser(@PathVariable Long user_id) {
        return goalService.getGoalsOfAUser(user_id);
    }

    @PutMapping("")
    public MessageResponse updateAGoal(@RequestBody GoalDTO goalDTO){
        return goalService.updateAGoal(goalDTO);
    }

    @PostMapping("/{user_id}")
    public MessageResponse createAGoal(@RequestBody GoalDTO goalDTO, @PathVariable Long user_id){
        return goalService.createAGoal(user_id,goalDTO);
    }

    @DeleteMapping("/{goal_id}")
    public MessageResponse deleteGoal(@PathVariable Long goal_id){
        return goalService.deleteGoal(goal_id);
    }

}


