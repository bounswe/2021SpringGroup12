package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.*;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping(value = "/goals", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/v2/goals")
public class GoalController {
    private final GoalService goalService;

    /************************************** GOALS*******/

    /*******************SEARCH BEGINS***********/
    @ApiOperation(value = "Search goals with exact match in title description and tag fields.")
    @GetMapping("/search")
    public List<GoalDTOShort> searchGoalsExact(@RequestParam(value = "query") @ApiParam(value = "Search query", example = "word1") String query) {
        return goalService.searchGoalsExact(query);
    }

    @ApiOperation(value = "Search in goals using tags")
    @GetMapping("/search/{tag}")
    public List<GoalDTOShort> searchGoalUsingTags(@PathVariable @ApiParam(value = "Search tag", example = "tag1") String tag) throws IOException, ParseException {
        return goalService.searchGoalUsingTags(tag);
    }
    /*******************SEARCH ENDS***********/
    @ApiOperation(value = "Get a Goal")
    @GetMapping("/{goal_id}")
    public GoalGetDTO getGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return goalService.getAGoal(goal_id);
    }

    @ApiOperation(value = "Get all goals of a user.")
    @GetMapping("/of_user/{user_id}")
    public List<GoalDTOShort> getGoalsOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
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
            )) GoalGetDTO goalGetDTO) {
        return goalService.updateAGoal(goalGetDTO);
    }

    @ApiOperation(value = "Add tags to a goal.")
    @PutMapping("/{goal_id}/tag")
    public MessageResponse addTags(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id,
                                   @RequestBody @ApiParam(value = "Set of tags as list.", example = "['tag1','tag2']") Set<String> tags) throws IOException, ParseException {
        return goalService.addTags(goal_id, tags);
    }

    @ApiOperation(value = "Create a goal.")
    @PostMapping("/{user_id}")
    public MessageResponse createAGoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"deadline*\": \"2021-11-20T10:01:10.538Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            )) GoalPostDTO goalPostDTO, @PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return goalService.createAGoal(user_id, goalPostDTO);
    }

    @ApiOperation(value = "Delete a goal.")
    @DeleteMapping("/{goal_id}")
    public MessageResponse deleteGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return goalService.deleteGoal(goal_id);
    }

    @ApiOperation(value = "Complete a goal.")
    @PutMapping("/complete/{goal_id}")
    public MessageResponse completeGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return goalService.completeGoal(goal_id);
    }

    /************************************** SUBGOALS *******/
    @ApiOperation(value = "Create a subgoal under a goal.")
    @PostMapping("/subgoal")
    public MessageResponse createSubgoal(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"main_goal_id*\": 1\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
            )
            )
    ) SubgoalPostDTO subgoal_dto) {
        return goalService.createSubgoal(subgoal_dto);
    }


    /********************* ANALYTICS **************/
    @ApiOperation(value = "Get analytics of a goal.")
    @GetMapping("/analytics/{goal_id}")
    public GoalAnalyticsDTO getAnalytics(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return goalService.getAnalytics(goal_id);
    }
}


