package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.goalspace.dto.GoalDTO;
import cmpe451.group12.beabee.goalspace.model.Goal;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping(value = "/goals", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/goals")
public class GoalController {
    private final GoalService goalService;

    @ApiOperation(value = "Get a Goal")
    @GetMapping("/{goal_id}")
    public GoalDTO getGoal(@PathVariable  @ApiParam(value = "Id of the goal.",example = "5") Long goal_id) {
        return goalService.getAGoal(goal_id);
    }

    @ApiOperation(value = "Get all goals of a user.")
    @GetMapping("/of_user/{user_id}")
    public List<GoalDTO> getGoalsOfAUser(@PathVariable @ApiParam(value = "Id of the user.",example = "5")Long user_id) {
        return goalService.getGoalsOfAUser(user_id);
    }

    @ApiOperation(value = "Update a goal.")
    @PutMapping("")
    public MessageResponse updateAGoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"createdAt*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"deadline*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"goalType\": \"GOAL\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            ) )GoalDTO goalDTO){
        return goalService.updateAGoal(goalDTO);
    }

    @ApiOperation(value = "Create a goal.")
    @PostMapping("/{user_id}")
    public MessageResponse createAGoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"createdAt*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"deadline*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"goalType\": \"GOAL\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            ) ) GoalDTO goalDTO, @PathVariable @ApiParam(value = "Id of the user.",example = "5")Long user_id){
        return goalService.createAGoal(user_id,goalDTO);
    }

    @ApiOperation(value = "Delete a goal.")
    @DeleteMapping("/{goal_id}")
    public MessageResponse deleteGoal(@PathVariable @ApiParam(value = "Id of the goal.",example = "5")Long goal_id){
        return goalService.deleteGoal(goal_id);
    }

}


