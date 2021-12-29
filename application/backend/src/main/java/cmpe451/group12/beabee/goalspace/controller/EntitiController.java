package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.DateDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.*;
import cmpe451.group12.beabee.goalspace.service.EntitiService;
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
//@RequestMapping(value = "/entities", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/v2/entities")
public class EntitiController {

    private final EntitiService entitiService;

    /****************************** GET ALL ENTITIES ********************************/


    @ApiOperation(value = "Get all entities of a Goal including entities of subgoals' and entities of subgoals' of subgoals")
    @GetMapping("/goal/{goal_id}")
    public List<EntitiDTOShort> getEntitiesOfAGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return entitiService.getEntitiesOfAGoal(goal_id);
    }

    @ApiOperation(value = "Get All Entities Of a User")
    @GetMapping("/user/{user_id}")
    public List<EntitiDTOShort> getEntitiesOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getEntitiesOfAUser(user_id);
    }

    /****************************** LINKING ENTITIES ********************************/

    @ApiOperation(value = "Link two entities.")
    @PostMapping("/{id}/link")
    public MessageResponse entitiLink(@PathVariable @ApiParam(value = "Id of the parent entity.", example = "5") Long id,
                                      @RequestBody @ApiParam(
                                                value = "JSON object representing the link.",
                                                examples = @Example(
                                                        value = @ExampleProperty(
                                                                value = "{\n" +
                                                                        "\t\"childId\": 1,\n" +
                                                                        "\t\"childType\": \"ENTITI\"\n" +
                                                                        "}"
                                                        )
                                                )
                                        ) EntitiLinkDTO entitiLinkDTO) {
        return entitiService.entitiLink(id, entitiLinkDTO);
    }

    @ApiOperation(value = "Delete a link between entities.")
    @DeleteMapping("/{id}/link")
    public MessageResponse entitiDeleteLink(@PathVariable @ApiParam(value = "Id of the parent entity.", example = "5") Long id,
                                            @RequestBody @ApiParam(
                                                    value = "JSON object representing the link.",
                                                    examples = @Example(
                                                            value = @ExampleProperty(
                                                                    value = "{\n" +
                                                                            "\t\"childId\": 1,\n" +
                                                                            "\t\"childType\": \"ENTITI\"\n" +
                                                                            "}"
                                                            )
                                                    )
                                            ) EntitiLinkDTO entitiLinkDTO) {
        return entitiService.entitiDeleteLink(id, entitiLinkDTO);
    }

    /****************************** POSTS ********************************/


    @ApiOperation(value = "Create a task.")
    @PostMapping("/task")
    public MessageResponse createTask(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"goalType\": \"GROUPGOAL\",\n" +
                            "    \"goalId\": 1,\n" +
                            "    \"initialLinkType\": \"ENTITI\",\n" +
                            "    \"initialParentId\": 1,\n" +
                            "    \"title\": \"Title\",\n" +
                            "    \"description\": \"Description\",\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\"\n" +
                            "}"
            )
            )) TaskPostDTO task_dto) {
        return entitiService.createTask(task_dto);
    }

    @ApiOperation(value = "Create a routine.")
    @PostMapping("/routine")
    public MessageResponse createRoutine(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"goalType\": \"GROUPGOAL\",\n" +
                            "    \"goalId\": 1,\n" +
                            "    \"initialLinkType\": \"ENTITI\",\n" +
                            "    \"initialParentId\": 1,\n" +
                            "    \"title\": \"Title\",\n" +
                            "    \"description\": \"Description\",\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"period\": 1\n" +
                            "}"
            )
            )) RoutinePostDTO routine_dto) {
        return entitiService.createRoutine(routine_dto);
    }

    @ApiOperation(value = "Create a reflection.")
    @PostMapping("/reflection")
    public MessageResponse createReflection(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"goalType\": \"GROUPGOAL\",\n" +
                            "    \"goalId\": 1,\n" +
                            "    \"initialLinkType\": \"ENTITI\",\n" +
                            "    \"initialParentId\": 1,\n" +
                            "    \"title\": \"Title\",\n" +
                            "    \"description\": \"Description\",\n" +
                            "}"
            )
            )) ReflectionPostDTO reflection_post_dto) {
        return entitiService.createReflection(reflection_post_dto);
    }

    @ApiOperation(value = "Create a question.")
    @PostMapping("/question")
    public MessageResponse createQuestion(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"goalType\": \"GROUPGOAL\",\n" +
                            "    \"goalId\": 1,\n" +
                            "    \"initialLinkType\": \"ENTITI\",\n" +
                            "    \"initialParentId\": 1,\n" +
                            "    \"title\": \"Title\",\n" +
                            "    \"description\": \"Description\",\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "}"
            )
            )) QuestionPostDTO question_dto) {
        return entitiService.createQuestion(question_dto);
    }

    /****************************** GETS ********************************/
    /*
    @ApiOperation(value = "Get an entity.")
    @GetMapping("/entiti/{id}")
    public EntitiDTO getEntiti(@PathVariable @ApiParam(value = "Id of the entity.",example = "5")Long id) {
        return entitiService.getEntiti(id);
    }
*/
    @ApiOperation(value = "Get a task.")
    @GetMapping("/task/{id}")
    public TaskGetDTO getTask(@PathVariable @ApiParam(value = "Id of the task.", example = "5") Long id) {
        return entitiService.getTask(id);
    }

    @ApiOperation(value = "Get a routine.")
    @GetMapping("/routine/{id}")
    public RoutineGetDTO getRoutine(@PathVariable @ApiParam(value = "Id of the routine.", example = "5") Long id) {
        return entitiService.getRoutine(id);
    }

    @ApiOperation(value = "Get a reflection.")
    @GetMapping("/reflection/{id}")
    public ReflectionGetDTO getReflection(@PathVariable @ApiParam(value = "Id of the reflection.", example = "5") Long id) {
        return entitiService.getReflection(id);
    }

    @ApiOperation(value = "Get a question.")
    @GetMapping("/question/{id}")
    public QuestionGetDTO getQuestion(@PathVariable @ApiParam(value = "Id of the question.", example = "5") Long id) {
        return entitiService.getQuestion(id);
    }

    /****************************** DELETES ********************************/

    @ApiOperation(value = "Delete a task.")
    @DeleteMapping("/task/{id}")
    public MessageResponse deleteTask(@PathVariable @ApiParam(value = "Id of the task.", example = "5") Long id) {
        return entitiService.deleteTask(id);
    }

    @ApiOperation(value = "Delete a routine.")
    @DeleteMapping("/routine/{id}")
    public MessageResponse deleteRoutine(@PathVariable @ApiParam(value = "Id of the routine.", example = "5") Long id) {
        return entitiService.deleteRoutine(id);
    }

    @ApiOperation(value = "Delete a reflection.")
    @DeleteMapping("/reflection/{id}")
    public MessageResponse deleteReflection(@PathVariable @ApiParam(value = "Id of the reflection.", example = "5") Long id) {
        return entitiService.deleteReflection(id);
    }

    @ApiOperation(value = "Delete a question.")
    @DeleteMapping("/question/{id}")
    public MessageResponse deleteQuestion(@PathVariable @ApiParam(value = "Id of the question.", example = "5") Long id) {
        return entitiService.deleteQuestion(id);
    }

    /****************************** PUTS ********************************/


    @ApiOperation(value = "Update a task.")
    @PutMapping("/task/")
    public MessageResponse updateTask(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"deadline*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"createdAt*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"entitiType*\": \"TASK\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"mainGoal_id*\": 0,\n" +
                            "  \"rating*\": 0,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            )) TaskGetDTO task_dto) {
        return entitiService.updateTask(task_dto);
    }

    @ApiOperation(value = "Update a routine.")
    @PutMapping("/routine/")
    public MessageResponse updateRoutine(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"deadline*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"createdAt*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"entitiType*\": \"ROUTINE\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"mainGoal_id*\": 0,\n" +
                            "  \"rating*\": 0,\n" +
                            "  \"period*\": 0,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            )) RoutineGetDTO routine_dto) {
        return entitiService.updateRoutine(routine_dto);
    }

    @ApiOperation(value = "Update a reflection.")
    @PutMapping("/reflection/")
    public MessageResponse updateReflection(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"createdAt*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"entitiType*\": \"REFLECTION\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"mainGoal_id*\": 0,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            )) ReflectionGetDTO reflection_dto) {
        return entitiService.updateReflection(reflection_dto);
    }

    @ApiOperation(value = "Update a question.")
    @PutMapping("/question/")
    public MessageResponse updateQuestion(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"createdAt*\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "  \"description*\": \"string\",\n" +
                            "  \"entitiType*\": \"QUESTION\",\n" +
                            "  \"id*\": 0,\n" +
                            "  \"isDone*\": true,\n" +
                            "  \"mainGoal_id*\": 0,\n" +
                            "  \"title*\": \"string\"\n" +
                            "}"
            )
            )) QuestionGetDTO question_dto) {
        return entitiService.updateQuestion(question_dto);
    }

    @ApiOperation(value = "Extend an entity.")
    @PutMapping("/extend/{entiti_id}")
    public MessageResponse extendEntiti(@PathVariable @ApiParam(value = "Id of the entity.", example = "5") Long entiti_id, @RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"newDeadline\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "}"
            )
            )) DateDTO dateDTO) {
        return entitiService.extendEntiti(entiti_id, dateDTO.getNewDeadline());
    }

    /********************************** ROUTINE RATE *****************/

    @ApiOperation(value = "Rate a routine.")
    @PutMapping("/rate/{routine_id}/{rating}")
    public MessageResponse rateRoutine(@PathVariable @ApiParam(value = "Id of the routine.", example = "5") Long routine_id, @PathVariable @ApiParam(value = "Rating of the routine.", example = "5") Long rating) {
        return entitiService.rateRoutine(routine_id, rating);
    }


    /********************************** COMPLETE ENTITIES*****************/
    @ApiOperation(value = "Complete a task.")
    @PutMapping("/completetask/{task_id}/{rating}")
    public MessageResponse completeTask(@PathVariable @ApiParam(value = "Id of the task.", example = "5") Long task_id, @PathVariable @ApiParam(value = "Rating of the task.", example = "5") Long rating) {
        return entitiService.completeTask(task_id, rating);
    }

    @ApiOperation(value = "Complete a routine.")
    @PutMapping("/completeroutine/{routine_id}/{rating}")
    public MessageResponse completeRoutine(@PathVariable @ApiParam(value = "Id of the routine.", example = "5") Long routine_id, @PathVariable @ApiParam(value = "Rating of the routine.", example = "5") Long rating) {
        return entitiService.completeRoutine(routine_id, rating);
    }

    @ApiOperation(value = "Complete a question.")
    @PutMapping("/completequestion/{question_id}")
    public MessageResponse completeQuestion(@PathVariable @ApiParam(value = "Id of the task.", example = "5") Long question_id) {
        return entitiService.completeQuestion(question_id);
    }

    @ApiOperation(value = "Complete a reflection.")
    @PutMapping("/completereflection/{reflection_id}")
    public MessageResponse completeRefection(@PathVariable @ApiParam(value = "Id of the reflection.", example = "5") Long reflection_id) {
        return entitiService.completeRefection(reflection_id);
    }

}
