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

import java.util.Date;
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
/*
    @ApiOperation(value = "Get All Entities Of a Goal")
    @GetMapping("/goal/{goal_id}")
    public List<EntitiDTO> getEntitiesOfAGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long goal_id) {
        return entitiService.getEntitiesOfAGoal(goal_id);
    }
*/
    @ApiOperation(value = "Get All Entities Of a User")
    @GetMapping("/user/{user_id}")
    public List<EntitiDTOShort> getEntitiesOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getEntitiesOfAUser(user_id);
    }

 /*
    @ApiOperation(value = "Get All Questions Of a User")
    @GetMapping("/user/question/{user_id}")
    public List<QuestionGetDTO> getQuestionsOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getQuestionsOfAUser(user_id);
    }

    @ApiOperation(value = "Get All Reflections Of a User")
    @GetMapping("/user/reflection/{user_id}")
    public List<ReflectionGetDTO> getReflectionsOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getReflectionsOfAUser(user_id);
    }

    @ApiOperation(value = "Get All Routines Of a User")
    @GetMapping("/user/routine/{user_id}")
    public List<RoutineGetDTO> getRoutinesOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getRoutinesOfAUser(user_id);
    }

    @ApiOperation(value = "Get All Tasks Of a User")
    @GetMapping("/user/task/{user_id}")
    public List<TaskGetDTO> getTasksOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return entitiService.getTasksOfAUser(user_id);
    }
*/

    /****************************** LINKING ENTITIES ********************************/

    @ApiOperation(value = "Link two entities.")
    @PostMapping("/{id}/link/{child_id}")
    public MessageResponse linkEntities(@PathVariable @ApiParam(value = "Id of the parent entity.", example = "5") Long id, @PathVariable @ApiParam(value = "Id of the child entity.", example = "5") Long child_id) {
        return entitiService.linkEntities(id, child_id);
    }

    /* no need for this, user can get the same result using GET any entity
    @ApiOperation(value = "Get all sublinks of an entity.")
    @GetMapping("/{entity_id}/sublinks")
    public List<EntitiDTO> getSublinksOfAnEntity(@PathVariable @ApiParam(value = "Id of the entity.",example = "5") Long entity_id) {
        return entitiService.getSublinksOfAnEntity(entity_id);
    }
*/
    @ApiOperation(value = "Delete a link between entities.")
    @DeleteMapping("/{id}/delete_link/{child_id}")
    public MessageResponse deleteSublinkOfAnEntity(@PathVariable @ApiParam(value = "Id of the parent entity.", example = "5") Long id, @PathVariable @ApiParam(value = "Id of the child entity.", example = "5") Long child_id) {
        return entitiService.deleteSublinkOfAnEntity(id, child_id);
    }

    /****************************** POSTS ********************************/


    @ApiOperation(value = "Create a task.")
    @PostMapping("/task")
    public MessageResponse createTask(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"entitiType\": \"TASK\",\n" +
                            "    \"mainGoal_id*\": 0,\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
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
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"entitiType\": \"ROUTINE\",\n" +
                            "    \"mainGoal_id*\": 0,\n" +
                            "    \"title*\": \"string\"\n" +
                            "    \"period*\": \"5\"\n" +
                            "  }"
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
                            "    \"description\": \"string\",\n" +
                            "    \"mainGoal_id*\": 0,\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
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
                            "    \"deadline\": \"2021-11-20T09:44:23.994Z\",\n" +
                            "    \"description\": \"string\",\n" +
                            "    \"entitiType\": \"QUESTION\",\n" +
                            "    \"mainGoal_id*\": 0,\n" +
                            "    \"title*\": \"string\"\n" +
                            "  }"
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

    /***************************************** EXTEND ****************************/
    @ApiOperation(value = "Extend an entity.")
    @PutMapping("/extend/{entiti_id}")
    public MessageResponse extendEntiti(@PathVariable @ApiParam(value = "Id of the entity.", example = "5")Long entiti_id, @RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"newDeadline\": \"2021-11-20T09:48:42.553Z\",\n" +
                            "}"
            )
            ))DateDTO dateDTO) {
        return entitiService.extendEntiti(entiti_id, dateDTO.getNewDeadline());
    }

}
