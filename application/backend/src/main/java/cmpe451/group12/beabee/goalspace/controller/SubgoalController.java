package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.DateDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.SubgoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.service.SubgoalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v2/subgoals")
public class SubgoalController {
    private final SubgoalService subgoalService;


    @ApiOperation(value = "Create a subgoal under another subgoal.")
    @PostMapping("")
    public MessageResponse createSubgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"parent_subgoal_id*\": 5,\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
            )
            )
    ) SubgoalPostDTO subgoal_dto) {
        return subgoalService.createSubgoal(subgoal_dto);
    }

    @ApiOperation(value = "Get a subgoal.")
    @GetMapping("/{id}")
    public SubgoalGetDTO getSubgoal(@PathVariable @ApiParam(value = "Id of the subgoal.",example = "5")Long id) {
        return subgoalService.getSubgoal(id);
    }


    @ApiOperation(value = "Get All Subgoals Of a User")
    @GetMapping("/of_user/{user_id}")
    public List<SubgoalGetDTO> getSubgoalsOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return subgoalService.getSubgoalsOfAUser(user_id);
    }

    /*
    @ApiOperation(value = "Get All Subgoals Of a Goal")
    @GetMapping("/of_goal/{goal_id}")
    public List<SubgoalDTOShort> getSubgoalsOfAGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return subgoalService.getSubgoalsOfAGoal(goal_id);
    }
    */
    /*
    @ApiOperation(value = "Get All Subgoals Of a Subgoal")
    @GetMapping("/of_subgoal/{subgoal_id}")
    public List<SubgoalDTOShort> getSubgoalsOfASubgoal(@PathVariable @ApiParam(value = "Id of the subgoal.", example = "5") Long subgoal_id) {
        return subgoalService.getSubgoalsOfASubgoal(subgoal_id);
    }
    */
    @ApiOperation(value = "Update a subgoal.")
    @PutMapping("")
    public MessageResponse updateSubgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"deadline*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"createdAt*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"parent_subgoal_id*\": 0,\n" +
                            "  \"rating*\": 0,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            ) ) SubgoalGetDTO subgoal_dto) {
        return subgoalService.updateSubgoal(subgoal_dto);
    }
    @ApiOperation(value = "Delete a subgoal.")
    @DeleteMapping("/{id}")
    public MessageResponse deleteSubgoal(@PathVariable @ApiParam(value = "Id of the subgoal.",example = "5")Long id) {
        return subgoalService.deleteSubgoal(id);
    }

    @ApiOperation(value = "Assign subgoal to the users.")
    @PostMapping("/{subgoal_id}/assignees")
    public MessageResponse addAssignees(@PathVariable @ApiParam(value = "Id of the subgoal.", example = "5") long subgoal_id,
                                        @RequestParam @ApiParam(value = "List if user ids", example = "5,13,24") List<Long> user_ids) {
        return subgoalService.addAssignees(subgoal_id, user_ids);
    }

    @ApiOperation(value = "Revoke the assignment of subgoal to the users.")
    @DeleteMapping("/{subgoal_id}/assignees")
    public MessageResponse removeAssigness(@PathVariable @ApiParam(value = "Id of the subgoal.", example = "5") long subgoal_id,
                                           @RequestParam @ApiParam(value = "List if user ids", example = "5,13,24") List<Long> user_ids) {
        return subgoalService.removeAssignees(subgoal_id, user_ids);
    }

    @ApiOperation(value = "Complete a subgoal.")
    @PutMapping("/complete/{subgoal_id}/{rating}")
    public MessageResponse completeSubgoal(@PathVariable @ApiParam(value = "Id of the subgoal.", example = "5") Long subgoal_id, @PathVariable @ApiParam(value = "Rating of the subgoal.", example = "5") Long rating) {
        return subgoalService.completeSubgoal(subgoal_id, rating);
    }

    /********************* ANALYTICS **************/
    @ApiOperation(value = "Get analytics of a subgoal.")
    @GetMapping("/analytics/{subgoal_id}")
    public SubgoalAnalyticsDTO getAnalytics(@PathVariable @ApiParam(value = "Id of the subgoal.", example = "5")Long subgoal_id){
        return subgoalService.getAnalytics(subgoal_id);
    }
}
