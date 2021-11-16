package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.*;
import cmpe451.group12.beabee.goalspace.dto.*;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.mapper.*;
import cmpe451.group12.beabee.goalspace.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntitiService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

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


    /****************************** GET ALL ENTITIES ********************************/

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

    public List<EntitiDTO> getEntitiesOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Entiti> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(entitiRepository.findAllByMainGoal(goalRepository.getById(g.getId())));
        }

        List<EntitiDTO> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Subgoal")).map(x -> entitiMapper.mapToDto((Subgoal) x)).collect(Collectors.toList()));
        return entity_dtos;
    }

    public List<SubgoalDTO> getSubgoalsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Subgoal> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(subgoalRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> subgoalMapper.mapToDto(x)).collect(Collectors.toList());
    }


    public List<QuestionDTO> getQuestionsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Question> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(questionRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> questionMapper.mapToDto(x)).collect(Collectors.toList());
    }

    public List<ReflectionDTO> getReflectionsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Reflection> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(reflectionRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> reflectionMapper.mapToDto(x)).collect(Collectors.toList());
    }

    public List<RoutineDTO> getRoutinesOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Routine> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(routineRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> routineMapper.mapToDto(x)).collect(Collectors.toList());
    }


    public List<TaskDTO> getTasksOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Task> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(taskRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> taskMapper.mapToDto(x)).collect(Collectors.toList());
    }

    /****************************** LINKING ENTITIES ********************************/

    public MessageResponse linkEntities(Long id, Long child_id) {
        Optional<Entiti> entity_opt = entitiRepository.findById(id);
        Optional<Entiti> child_entity_opt = entitiRepository.findById(child_id);
        if (entity_opt.isEmpty() || child_entity_opt.isEmpty()) {
            return new MessageResponse("One of the entities does not exists!", MessageType.ERROR);
        }
        Entiti entity = entity_opt.get();
        Set<Entiti> sublinks = entity.getSublinks();
        sublinks.add(child_entity_opt.get());
        entity.setSublinks(sublinks);
        entitiRepository.save(entity);
        return new MessageResponse("Linking operation is successful.", MessageType.SUCCESS);
    }

    public List<EntitiDTO> getSublinksOfAnEntity(Long entity_id) {
        if (!entitiRepository.existsById(entity_id)) {
            return new ArrayList<>();
        }
        List<Entiti> entities = entitiRepository.findById(entity_id).get().getSublinks().stream().collect(Collectors.toList());
        for (int i = 0; i < entities.size(); i++) {
            System.out.println(entities.get(i));
        }
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
        if (sublinks.contains(child_entity.get())) {
            sublinks.remove(child_entity.get());
            entity.get().setSublinks(sublinks);
            entitiRepository.save(entity.get());
            return new MessageResponse("Link deleted.", MessageType.SUCCESS);
        } else {
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
        new_subgoal.setEntitiType(EntitiType.SUBGOAL);
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
        new_task.setEntitiType(EntitiType.TASK);
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
        new_reflection.setEntitiType(EntitiType.REFLECTION);
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
        new_question.setEntitiType(EntitiType.QUESTION);
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
        new_routine.setEntitiType(EntitiType.ROUTINE);
        routineRepository.save(new_routine);
        return new MessageResponse("Routine added.", MessageType.SUCCESS);
    }

    /****************************** GETS ********************************/
    public EntitiDTO getEntiti(Long id) {
        Optional<Entiti> entiti_opt = entitiRepository.findById(id);
        if (entiti_opt.isEmpty()) {
            return new EntitiDTO();
        }
        if (entiti_opt.get().getEntitiType().equals(EntitiType.ROUTINE)) {
            return entitiMapper.mapToDto((Routine) entiti_opt.get());
        }
        if (entiti_opt.get().getEntitiType().equals(EntitiType.TASK)) {
            return entitiMapper.mapToDto((Task) entiti_opt.get());
        }
        if (entiti_opt.get().getEntitiType().equals(EntitiType.QUESTION)) {
            return entitiMapper.mapToDto((Question) entiti_opt.get());
        }
        if (entiti_opt.get().getEntitiType().equals(EntitiType.REFLECTION)) {
            return entitiMapper.mapToDto((Reflection) entiti_opt.get());
        }
        if (entiti_opt.get().getEntitiType().equals(EntitiType.SUBGOAL)) {
            return entitiMapper.mapToDto((Subgoal) entiti_opt.get());
        }
        return new EntitiDTO();// it wont reach here anyways
    }

    public SubgoalDTO getSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        if (subgoal_opt.isEmpty()){
            return new SubgoalDTO();
        }
        SubgoalDTO subgoalDTO = subgoalMapper.mapToDto(subgoal_opt.get());
        subgoalDTO.setMainGoal_id(subgoal_opt.get().getMainGoal().getId());
        return subgoalDTO;
    }

    public TaskDTO getTask(Long id) {
        Optional<Task> task_opt = taskRepository.findById(id);
        if (task_opt.isEmpty()){
            return new TaskDTO();
        }
        TaskDTO taskDTO = taskMapper.mapToDto(task_opt.get());
        taskDTO.setMainGoal_id(task_opt.get().getMainGoal().getId());
        return taskDTO;
    }

    public RoutineDTO getRoutine(Long id) {
        Optional<Routine> routine_opt = routineRepository.findById(id);
        if (routine_opt.isEmpty()){
            return new RoutineDTO();
        }
        RoutineDTO routineDTO = routineMapper.mapToDto(routine_opt.get());
        routineDTO.setMainGoal_id(routine_opt.get().getMainGoal().getId());
        return routineDTO;
    }

    public ReflectionDTO getReflection(Long id) {
        Optional<Reflection> reflection_opt = reflectionRepository.findById(id);
        if (reflection_opt.isEmpty()){
            return new ReflectionDTO();
        }
        ReflectionDTO reflectionDTO = reflectionMapper.mapToDto(reflection_opt.get());
        reflectionDTO.setMainGoal_id(reflection_opt.get().getMainGoal().getId());
        return reflectionDTO;
    }

    public QuestionDTO getQuestion(Long id) {
        Optional<Question> question_opt = questionRepository.findById(id);
        if (question_opt.isEmpty()){
            return new QuestionDTO();
        }
        QuestionDTO questionDTO = questionMapper.mapToDto(question_opt.get());
        questionDTO.setMainGoal_id(question_opt.get().getMainGoal().getId());
        return questionDTO;
    }

    /****************************** DELETES ********************************/

    public MessageResponse deleteReflection(Long id) {
        Optional<Reflection>  reflection_opt = reflectionRepository.findById(id);
        if (reflection_opt.isEmpty()) {
            return new MessageResponse("Reflection not found!", MessageType.ERROR);
        }
        Goal main_goal = reflection_opt.get().getMainGoal();
        Set<Entiti> entities = main_goal.getEntities();
        entities.remove(reflection_opt.get());
        main_goal.setEntities(entities);
        goalRepository.save(main_goal);
        reflectionRepository.deleteById(id);
        return new MessageResponse("Reflection deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteQuestion(Long id) {
        Optional<Question>  question_opt = questionRepository.findById(id);
        if (question_opt.isEmpty()) {
            return new MessageResponse("Question not found!", MessageType.ERROR);
        }
        Goal main_goal = question_opt.get().getMainGoal();
        Set<Entiti> entities = main_goal.getEntities();
        entities.remove(question_opt.get());
        main_goal.setEntities(entities);
        goalRepository.save(main_goal);
        questionRepository.deleteById(id);
        return new MessageResponse("Question deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        if (subgoal_opt.isEmpty()) {
            return new MessageResponse("Subgoal not found!", MessageType.ERROR);
        }
        Goal main_goal = subgoal_opt.get().getMainGoal();
        Set<Entiti> entities = main_goal.getEntities();
        entities.remove(subgoal_opt.get());
        main_goal.setEntities(entities);
        goalRepository.save(main_goal);
        subgoalRepository.deleteById(id);
        return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);

}

    public MessageResponse deleteTask(Long id) {
        Optional<Task>  task_opt = taskRepository.findById(id);
        if (task_opt.isEmpty()) {
            return new MessageResponse("Task not found!", MessageType.ERROR);
        }
        Goal main_goal = task_opt.get().getMainGoal();
        Set<Entiti> entities = main_goal.getEntities();
        entities.remove(task_opt.get());
        main_goal.setEntities(entities);
        goalRepository.save(main_goal);
        taskRepository.deleteById(id);
        return new MessageResponse("Task deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteRoutine(Long id) {
        Optional<Routine>  routine_opt = routineRepository.findById(id);
        if (routine_opt.isEmpty()) {
            return new MessageResponse("Routine not found!", MessageType.ERROR);
        }
        Goal main_goal = routine_opt.get().getMainGoal();
        Set<Entiti> entities = main_goal.getEntities();
        entities.remove(routine_opt.get());
        main_goal.setEntities(entities);
        goalRepository.save(main_goal);
        routineRepository.deleteById(id);
        return new MessageResponse("Routine deleted!", MessageType.SUCCESS);
    }

    /****************************** PUTS ********************************/
    public MessageResponse updateSubgoal(SubgoalDTO subgoal_dto) {
        if (subgoalRepository.existsById(subgoal_dto.getId())) {
            Subgoal subgoal = subgoalMapper.mapToEntity(subgoal_dto);
            Goal goal = goalRepository.getById(subgoal_dto.getMainGoal_id());
            subgoal.setMainGoal(goal);
            subgoalRepository.save(subgoal);
            return new MessageResponse("Updated subgoal", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update subgoal!", MessageType.ERROR);
    }


    public MessageResponse updateTask(TaskDTO task_dto) {
        if (taskRepository.existsById(task_dto.getId())) {
            Task task = taskMapper.mapToEntity(task_dto);
            Goal goal = goalRepository.getById(task_dto.getMainGoal_id());
            task.setMainGoal(goal);
            taskRepository.save(task);
            return new MessageResponse("Updated task", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update task!", MessageType.ERROR);
    }

    public MessageResponse updateRoutine(RoutineDTO routine_dto) {
        if (routineRepository.existsById(routine_dto.getId())) {
            Routine routine = routineMapper.mapToEntity(routine_dto);
            Goal goal = goalRepository.getById(routine_dto.getMainGoal_id());
            routine.setMainGoal(goal);
            routineRepository.save(routine);
            return new MessageResponse("Updated routine", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update routine!", MessageType.ERROR);
    }

    public MessageResponse updateReflection(ReflectionDTO reflection_dto) {
        if (reflectionRepository.existsById(reflection_dto.getId())) {
            Reflection reflection = reflectionMapper.mapToEntity(reflection_dto);
            Goal goal = goalRepository.getById(reflection_dto.getMainGoal_id());
            reflection.setMainGoal(goal);
            reflectionRepository.save(reflection);
            return new MessageResponse("Updated reflection", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update reflection!", MessageType.ERROR);
    }

    public MessageResponse updateQuestion(QuestionDTO question_dto) {
        if (questionRepository.existsById(question_dto.getId())) {
            Question question = questionMapper.mapToEntity(question_dto);
            Goal goal = goalRepository.getById(question_dto.getMainGoal_id());
            question.setMainGoal(goal);
            questionRepository.save(question);
            return new MessageResponse("Updated question", MessageType.SUCCESS);
        }
        return new MessageResponse("Couldn't update question!", MessageType.ERROR);
    }


}
