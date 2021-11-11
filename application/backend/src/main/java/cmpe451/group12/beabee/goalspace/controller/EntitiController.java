package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.*;
import cmpe451.group12.beabee.goalspace.service.EntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping(value = "/entities", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/entities")
public class EntitiController {

    private final EntityService entityService;

    @GetMapping("/{goal_id}")
    public List<EntitiDTO> getEntitiesOfAGoal(@PathVariable Long goal_id) {
        return entityService.getEntitiesOfAGoal(goal_id);
    }

    /****************************** LINKING ENTITIES ********************************/
    @PostMapping("/{id}/link/{child_id}")
    public MessageResponse linkEntities(@PathVariable Long id, @PathVariable Long child_id){
        return entityService.linkEntities(id,child_id);
    }

    @GetMapping("/{entity_id}/sublinks")
    public List<EntitiDTO> getSublinksOfAnEntity(@PathVariable Long entity_id){
        return entityService.getSublinksOfAnEntity(entity_id);
    }
    @DeleteMapping("/{id}/delete_link/{child_id}")
    public MessageResponse deleteSublinkOfAnEntity(@PathVariable Long id, @PathVariable Long child_id){
        return entityService.deleteSublinkOfAnEntity(id,child_id);
    }

    /****************************** POSTS ********************************/
    @PostMapping("/subgoal")
    public MessageResponse createSubgoal(@RequestBody SubgoalDTO subgoal_dto) {
        return entityService.createSubgoal(subgoal_dto);
    }

    @PostMapping("/task")
    public MessageResponse createTask(@RequestBody TaskDTO task_dto) {
        return entityService.createTask(task_dto);
    }

    @PostMapping("/routine")
    public MessageResponse createRoutine(@RequestBody RoutineDTO routine_dto) {
        return entityService.createRoutine(routine_dto);
    }

    @PostMapping("/reflection")
    public MessageResponse createReflection(@RequestBody ReflectionDTO reflection_dto) {
        return entityService.createReflection(reflection_dto);
    }

    @PostMapping("/question")
    public MessageResponse createQuestion(@RequestBody QuestionDTO question_dto) {
        return entityService.createQuestion(question_dto);
    }

    /****************************** GETS ********************************/
    @GetMapping("/subgoal/{id}")
    public SubgoalDTO getSubgoal(@PathVariable Long id) {
        return entityService.getSubgoal(id);
    }

    @GetMapping("/task/{id}")
    public TaskDTO getTask(@PathVariable Long id) {
        return entityService.getTask(id);
    }

    @GetMapping("/routine/{id}")
    public RoutineDTO getRoutine(@PathVariable Long id) {
        return entityService.getRoutine(id);
    }

    @GetMapping("/reflection/{id}")
    public ReflectionDTO getReflection(@PathVariable Long id) {
        return entityService.getReflection(id);
    }

    @GetMapping("/question/{id}")
    public QuestionDTO getQuestion(@PathVariable Long id) {
        return entityService.getQuestion(id);
    }

    /****************************** DELETES ********************************/
    @DeleteMapping("/subgoal/{id}")
    public MessageResponse deleteSubgoal(@PathVariable Long id) {
        return entityService.deleteSubgoal(id);
    }

    @DeleteMapping("/task/{id}")
    public MessageResponse deleteTask(@PathVariable Long id) {
        return entityService.deleteTask(id);
    }

    @DeleteMapping("/routine/{id}")
    public MessageResponse deleteRoutine(@PathVariable Long id) {
        return entityService.deleteRoutine(id);
    }

    @DeleteMapping("/reflection/{id}")
    public MessageResponse deleteReflection(@PathVariable Long id) {
        return entityService.deleteReflection(id);
    }

    @DeleteMapping("/question/{id}")
    public MessageResponse deleteQuestion(@PathVariable Long id) {
        return entityService.deleteQuestion(id);
    }

    /****************************** PUTS ********************************/
    @PutMapping("/subgoal/{id}")
    public MessageResponse updateSubgoal(@RequestBody SubgoalDTO subgoal_dto) {
        return entityService.updateSubgoal(subgoal_dto);
    }

    @PutMapping("/task/{id}")
    public MessageResponse updateTask(@RequestBody TaskDTO task_dto) {
        return entityService.updateTask(task_dto);
    }

    @PutMapping("/routine/{id}")
    public MessageResponse updateRoutine(@RequestBody RoutineDTO routine_dto) {
        return entityService.updateRoutine(routine_dto);
    }

    @PutMapping("/reflection/{id}")
    public MessageResponse updateReflection(@RequestBody ReflectionDTO reflection_dto) {
        return entityService.updateReflection(reflection_dto);
    }

    @PutMapping("/question/{id}")
    public MessageResponse updateQuestion(@RequestBody QuestionDTO question_dto) {
        return entityService.updateQuestion(question_dto);
    }


}
