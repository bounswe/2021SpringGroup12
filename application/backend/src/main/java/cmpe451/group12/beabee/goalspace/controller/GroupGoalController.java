package cmpe451.group12.beabee.goalspace.controller;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.goals.*;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.service.GroupGoalService;
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
//@RequestMapping(value = "/goals", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/v2/groupgoals")
public class GroupGoalController {
    private final GroupGoalService groupGoalService;

    /************************************** GOALS*******/
    @ApiOperation(value = "Get a Group Goal")
    @GetMapping("/{groupgoal_id}")
    public GroupGoalGetDto getAGroupgoal(@PathVariable @ApiParam(value = "Id of the group goal.",example = "5") Long groupgoal_id) {
        return groupGoalService.getAGroupgoal(groupgoal_id);
    }

    @ApiOperation(value = "Get all group goals created by a user.")
    @GetMapping("/created_by/{user_id}")
    public List<GroupGoalDTOShort> getGroupgoalsOfAUser(@PathVariable @ApiParam(value = "Id of the user.",example = "5")Long user_id) {
        return groupGoalService.getGroupgoalsCreatedByAUser(user_id);
    }


    @ApiOperation(value = "Get all group goals a user is member of.")
    @GetMapping("/member_of/{user_id}")
    public List<GroupGoalDTOShort> getGroupgoalsAUserMemberOf(@PathVariable @ApiParam(value = "Id of the user.",example = "5")Long user_id) {
        return groupGoalService.getGroupgoalsOfAUser(user_id);
    }

    @ApiOperation(value = "Generate a new token for the group goal.")
    @PostMapping("/{groupgoal_id}/token")
    public MessageResponse regenerateToken(@PathVariable @ApiParam(value = "Id of the group goal", example = "5") Long groupgoal_id) {
        return groupGoalService.regenerateToken(groupgoal_id);
    }

    @ApiOperation(value = "Update a group goal.")
    @PutMapping("")
    public MessageResponse updateAGroupgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"createdAt*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"deadline*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"goalType\": \"GROUPGOAL\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            ) ) GroupGoalGetDto groupGoalGetDto){
        return groupGoalService.updateAGroupgoal(groupGoalGetDto);
    }

    @ApiOperation(value = "Create a group goal.")
    @PostMapping("/{user_id}")
    public MessageResponse createAGroupgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"deadline*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            ) ) GroupGoalPostDTO groupGoalPostDTO, @PathVariable @ApiParam(value = "Id of the user.",example = "5")Long user_id){
        return groupGoalService.createAGroupgoal(user_id, groupGoalPostDTO);
    }

    @ApiOperation(value = "Delete a goal.")
    @DeleteMapping("/{groupgoal_id}")
    public MessageResponse deleteGroupgoal(@PathVariable @ApiParam(value = "Id of the group goal.",example = "5")Long groupgoal_id){
        return groupGoalService.deleteGroupgoal(groupgoal_id);
    }

    @ApiOperation(value = "Join a group goal with token")
    @PostMapping("/{user_id}/join")
    public GroupGoalDTOShort joinWithToken(
            @PathVariable @ApiParam(value = "Id of the user", example = "5") Long user_id,
            @RequestParam(value = "token") @ApiParam(value = "Token of the group", example = "5wbwf6yUxVBcr48AMbz9cb") String token){
        return groupGoalService.joinWithToken(user_id, token);
    }

    @ApiOperation(value = "Add a new member to the Group Goal")
    @PutMapping("/{groupgoal_id}/members")
    public MessageResponse addMember(
            @PathVariable @ApiParam(value = "Id of the group goal", example = "5") Long groupgoal_id,
            @RequestParam @ApiParam(value = "Username of the user", example = "5") String username) {
        return groupGoalService.addMember(groupgoal_id, username);
    }


    @ApiOperation(value = "Leave a group goal")
    @DeleteMapping("/{user_id}/{groupgoal_id}")
    public MessageResponse leaveGroupGoal(
            @PathVariable @ApiParam(value = "Id of the user", example = "5") Long user_id,
            @PathVariable @ApiParam(value = "Id of the group goal", example = "5") Long groupgoal_id)  {
        return groupGoalService.leaveGroupGoal(groupgoal_id, user_id);
    }

    /************************************** SUBGOALS *******/
    @ApiOperation(value = "Create a subgoal under a group goal.")
    @PostMapping("/subgoal")
    public MessageResponse createSubgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"main_groupgoal_id*\": 1\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
            )
            )
    ) SubgoalPostDTO subgoal_dto) {
        return groupGoalService.createSubgoal(subgoal_dto);
    }
}
