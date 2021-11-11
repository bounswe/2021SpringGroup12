package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.*;
import cmpe451.group12.beabee.goalspace.dto.*;
import cmpe451.group12.beabee.goalspace.enums.EntityType;
import cmpe451.group12.beabee.goalspace.mapper.*;
import cmpe451.group12.beabee.goalspace.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntityService {

    private final GoalRepository goalRepository;

    private final EntitiMapper entitiMapper;
    private final EntitiRepository entitiRepository;

    private final SubgoalMapper subgoalMapper;
    private final SubgoalRepository subgoalRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ReflectionRepository reflectionRepository;
    private final ReflectionMapper reflectionMapper;
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;


    public List<EntitiDTO> getEntitiesOfAGoal(Long goal_id) {
        if (!goalRepository.existsById(goal_id)) {
            return new ArrayList<>();
        }

        List<Entiti> entities = entitiRepository.findAllByMainGoal(goalRepository.getById(goal_id));
        List<EntitiDTO> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Subgoal")).map(x -> entitiMapper.mapToDto((Subgoal) x)).collect(Collectors.toList()));
        return entity_dtos;
    }

    /****************************** LINKING ENTITIES ********************************/

    public MessageResponse linkEntities(Long id, Long child_id) {
        Optional<Entiti> entity = entitiRepository.findById(id);
        Optional<Entiti> child_entity = entitiRepository.findById(child_id);
        if (entity.isEmpty() || child_entity.isEmpty()) {
            return new MessageResponse("One of the entities does not exists!", MessageType.ERROR);
        }
        Set<Entiti> sublinks = entity.get().getSublinks();
        sublinks.add(child_entity.get());
        entity.get().setSublinks(sublinks);
        entitiRepository.save(entity.get());
        return new MessageResponse("Linking operation is successful.", MessageType.SUCCESS);
    }

    public List<EntitiDTO> getSublinksOfAnEntity(Long entity_id) {
        if (!entitiRepository.existsById(entity_id)) {
            return new ArrayList<>();
        }
        List<Entiti> entities = entitiRepository.findById(entity_id).get().getSublinks().stream().collect(Collectors.toList());
        List<EntitiDTO> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Subgoal")).map(x -> entitiMapper.mapToDto((Subgoal) x)).collect(Collectors.toList()));
        return entity_dtos;
    }

    public MessageResponse deleteSublinkOfAnEntity(Long id, Long child_id) {
        Optional<Entiti> entity = entitiRepository.findById(id);
        Optional<Entiti> child_entity = entitiRepository.findById(child_id);
        if (entity.isEmpty() || child_entity.isEmpty()) {
            return new MessageResponse("One of the entities does not exists!", MessageType.ERROR);
        }
            Set<Entiti> sublinks = entity.get().getSublinks();
            if (sublinks.contains(child_entity.get())){
                sublinks.remove(child_entity.get());
                entity.get().setSublinks(sublinks);
                entitiRepository.save(entity.get());
                return new MessageResponse("Link deleted.", MessageType.SUCCESS);
            }else{
                return new MessageResponse("There were no link at all!", MessageType.ERROR);
            }
    }

    /****************************** POSTS ********************************/

    public MessageResponse createSubgoal(SubgoalDTO subgoalDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(subgoalDTO.getMainGoal_id());
        if (goal_opt.isEmpty()) {
            return new MessageResponse("Goal not found!", MessageType.ERROR);
        }
        Subgoal new_subgoal = subgoalMapper.mapToEntity(subgoalDTO);
        new_subgoal.setMainGoal(goal_opt.get());
        new_subgoal.setEntityType(EntityType.SUBGOAL);
        subgoalRepository.save(new_subgoal);
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    public MessageResponse createTask(TaskDTO taskDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(taskDTO.getMainGoal_id());
        if (goal_opt.isEmpty()) {
            return new MessageResponse("Goal not found!", MessageType.ERROR);
        }
        Task new_task = taskMapper.mapToEntity(taskDTO);
        new_task.setMainGoal(goal_opt.get());
        new_task.setEntityType(EntityType.TASK);
        taskRepository.save(new_task);
        return new MessageResponse("Task added.", MessageType.SUCCESS);
    }

    public MessageResponse createReflection(ReflectionDTO reflectionDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(reflectionDTO.getMainGoal_id());
        if (goal_opt.isEmpty()) {
            return new MessageResponse("Goal not found!", MessageType.ERROR);
        }
        Reflection new_reflection = reflectionMapper.mapToEntity(reflectionDTO);
        new_reflection.setMainGoal(goal_opt.get());
        new_reflection.setEntityType(EntityType.REFLECTION);
        reflectionRepository.save(new_reflection);
        return new MessageResponse("Reflection added.", MessageType.SUCCESS);
    }

    public MessageResponse createQuestion(QuestionDTO questionDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(questionDTO.getMainGoal_id());
        if (goal_opt.isEmpty()) {
            return new MessageResponse("Goal not found!", MessageType.ERROR);
        }
        Question new_question = questionMapper.mapToEntity(questionDTO);
        new_question.setMainGoal(goal_opt.get());
        new_question.setEntityType(EntityType.QUESTION);
        questionRepository.save(new_question);
        return new MessageResponse("Question added.", MessageType.SUCCESS);
    }

    public MessageResponse createRoutine(RoutineDTO routineDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(routineDTO.getMainGoal_id());
        if (goal_opt.isEmpty()) {
            return new MessageResponse("Goal not found!", MessageType.ERROR);
        }
        Routine new_routine = routineMapper.mapToEntity(routineDTO);
        new_routine.setMainGoal(goal_opt.get());
        new_routine.setEntityType(EntityType.ROUTINE);
        routineRepository.save(new_routine);
        return new MessageResponse("Routine added.", MessageType.SUCCESS);
    }

    /****************************** GETS ********************************/

    public SubgoalDTO getSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        return subgoal_opt.isEmpty() ? new SubgoalDTO() : subgoalMapper.mapToDto(subgoal_opt.get());
    }

    public TaskDTO getTask(Long id) {
        Optional<Task> task_opt = taskRepository.findById(id);
        return task_opt.isEmpty() ? new TaskDTO() : taskMapper.mapToDto(task_opt.get());
    }

    public RoutineDTO getRoutine(Long id) {
        Optional<Routine> routine_opt = routineRepository.findById(id);
        return routine_opt.isEmpty() ? new RoutineDTO() : routineMapper.mapToDto(routine_opt.get());
    }

    public ReflectionDTO getReflection(Long id) {
        Optional<Reflection> reflection_opt = reflectionRepository.findById(id);
        return reflection_opt.isEmpty() ? new ReflectionDTO() : reflectionMapper.mapToDto(reflection_opt.get());
    }

    public QuestionDTO getQuestion(Long id) {
        Optional<Question> question_opt = questionRepository.findById(id);
        return question_opt.isEmpty() ? new QuestionDTO() : questionMapper.mapToDto(question_opt.get());
    }

    /****************************** DELETES ********************************/

    public MessageResponse deleteReflection(Long id) {
        try {
            reflectionRepository.deleteById(id);
            return new MessageResponse("Reflection deleted!", MessageType.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Reflection not found!", MessageType.ERROR);
        }
    }

    public MessageResponse deleteQuestion(Long id) {
        try {
            questionRepository.deleteById(id);
            return new MessageResponse("Question deleted!", MessageType.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Question not found!", MessageType.ERROR);
        }
    }

    public MessageResponse deleteSubgoal(Long id) {
        try {
            subgoalRepository.deleteById(id);
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Subgoal not found!", MessageType.ERROR);
        }
    }

    public MessageResponse deleteTask(Long id) {
        try {
            taskRepository.deleteById(id);
            return new MessageResponse("Task deleted!", MessageType.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Task not found!", MessageType.ERROR);
        }
    }

    public MessageResponse deleteRoutine(Long id) {
        try {
            routineRepository.deleteById(id);
            return new MessageResponse("Routine deleted!", MessageType.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return new MessageResponse("Routine not found!", MessageType.ERROR);
        }
    }

    /****************************** PUTS ********************************/
    public MessageResponse updateSubgoal(SubgoalDTO subgoal_dto) {
        if (subgoalRepository.existsById(subgoal_dto.getId())) {
            Subgoal subgoal = subgoalMapper.mapToEntity(subgoal_dto);
            subgoalRepository.save(subgoal);
            return new MessageResponse("Updated subgoal", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update subgoal!", MessageType.ERROR);
    }


    public MessageResponse updateTask(TaskDTO task_dto) {
        if (taskRepository.existsById(task_dto.getId())) {
            Task task = taskMapper.mapToEntity(task_dto);
            taskRepository.save(task);
            return new MessageResponse("Updated task", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update task!", MessageType.ERROR);
    }

    public MessageResponse updateRoutine(RoutineDTO routine_dto) {
        if (routineRepository.existsById(routine_dto.getId())) {
            Routine routine = routineMapper.mapToEntity(routine_dto);
            routineRepository.save(routine);
            return new MessageResponse("Updated routine", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update routine!", MessageType.ERROR);
    }

    public MessageResponse updateReflection(ReflectionDTO reflection_dto) {
        if (reflectionRepository.existsById(reflection_dto.getId())) {
            Reflection reflection = reflectionMapper.mapToEntity(reflection_dto);
            reflectionRepository.save(reflection);
            return new MessageResponse("Updated reflection", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update reflection!", MessageType.ERROR);
    }

    public MessageResponse updateQuestion(QuestionDTO question_dto) {
        if (questionRepository.existsById(question_dto.getId())) {
            Question question = questionMapper.mapToEntity(question_dto);
            questionRepository.save(question);
            return new MessageResponse("Updated question", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update question!", MessageType.ERROR);
    }


}
