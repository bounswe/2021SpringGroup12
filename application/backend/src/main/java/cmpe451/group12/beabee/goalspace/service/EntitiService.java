package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.*;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.mapper.entities.*;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntitiService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    private final EntitiMapper entitiMapper;
    private final EntitiShortMapper entitiShortMapper;
    private final EntitiRepository entitiRepository;

    private final SubgoalGetMapper subgoalGetMapper;
    private final SubgoalRepository subgoalRepository;
    private final TaskRepository taskRepository;
    private final TaskGetMapper taskGetMapper;
    private final ReflectionRepository reflectionRepository;
    private final ReflectionGetMapper reflectionGetMapper;
    private final ReflectionPostMapper reflectionPostMapper;
    private final TaskPostMapper taskPostMapper;
    private final QuestionPostMapper questionPostMapper;
    private final RoutinePostMapper routinePostMapper;
    private final RoutineRepository routineRepository;
    private final RoutineGetMapper routineGetMapper;
    private final QuestionRepository questionRepository;
    private final QuestionGetMapper questionGetMapper;

    private final ResourceRepository resourceRepository;
    private final ResourceShortMapper resourceShortMapper;

    /****************************** GET ALL ENTITIES ********************************/

    /* no need for this. They can already get this info on GET a goal endpoint.

    public List<EntitiDTO> getEntitiesOfAGoal(Long goal_id) {
        if (!goalRepository.existsById(goal_id)) {
            return new ArrayList<>();
        }

        List<Entiti> entities = entitiRepository.findAllByGoal(goalRepository.getById(goal_id));
        List<EntitiDTO> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        return entity_dtos;
    }
*/

    public List<EntitiDTOShort> getEntitiesOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        Users user = userRepository.getById(user_id);
        List<Subgoal> subgoals_of_user = subgoalRepository.findAllByCreator(user);
        List<Goal> goals_of_user = goalRepository.findAllByUserId(user_id);


        List<Entiti> entities = new ArrayList<>();
        for (Goal g : goals_of_user) {
            entities.addAll(g.getEntities());
        }
        for (Subgoal s : subgoals_of_user) {
            entities.addAll(s.getEntities());
        }

        List<EntitiDTOShort> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        return entity_dtos;
    }

   /*TODO: this is complex. Need to implement recursive queries. Or maybe I can just add user_id field to all entities and subgoals. Will think about it.

    public List<QuestionGetDTO> getQuestionsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Question> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(questionRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> questionGetMapper.mapToDto(x)).collect(Collectors.toList());
    }

    public List<ReflectionGetDTO> getReflectionsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Reflection> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(reflectionRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> reflectionGetMapper.mapToDto(x)).collect(Collectors.toList());
    }

    public List<RoutineGetDTO> getRoutinesOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Routine> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(routineRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> routineGetMapper.mapToDto(x)).collect(Collectors.toList());
    }


    public List<TaskGetDTO> getTasksOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ArrayList<>();
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Task> entities = new ArrayList<>();
        for (Goal g : goals) {
            entities.addAll(taskRepository.findByGoalId(g.getId()));
        }
        return entities.stream().map(x -> taskGetMapper.mapToDto(x)).collect(Collectors.toList());
    }


    */
    /****************************** LINKING ENTITIES ********************************/

    public MessageResponse linkEntities(Long id, Long child_id) {
        Optional<Entiti> entity_opt = entitiRepository.findById(id);
        Optional<Entiti> child_entity_opt = entitiRepository.findById(child_id);
        if (entity_opt.isEmpty() || child_entity_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of the entities does not exists!");
        }
        Entiti entity = entity_opt.get();
        Set<Entiti> sublinks = entity.getSublinks();
        sublinks.add(child_entity_opt.get());
        entity.setSublinks(sublinks);
        entitiRepository.save(entity);
        return new MessageResponse("Linking operation is successful.", MessageType.SUCCESS);
    }

    /* no need, since we return sublinks in GET entity endpoint
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
        return entity_dtos;
    }
*/

    public MessageResponse deleteSublinkOfAnEntity(Long id, Long child_id) {
        Optional<Entiti> entity = entitiRepository.findById(id);
        Optional<Entiti> child_entity = entitiRepository.findById(child_id);
        if (entity.isEmpty() || child_entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One of the entities does not exists!");
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

    public MessageResponse createTask(TaskPostDTO taskPostDTO) {
        if (taskPostDTO.getGoal_id() == null && taskPostDTO.getSubgoal_id() == null) {
            return new MessageResponse("This entity must belong to a goal or a subgoal!", MessageType.ERROR);
        }
        Task new_task = taskPostMapper.mapToEntity(taskPostDTO);
        new_task.setEntitiType(EntitiType.TASK);
        new_task.setIsDone(Boolean.FALSE);
        if (taskPostDTO.getGoal_id() == null) {
            Optional<Subgoal> subgoal_opt = subgoalRepository.findById(taskPostDTO.getSubgoal_id());
            if (subgoal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
            }
            new_task.setSubgoal(subgoal_opt.get());
        } else {
            Optional<Goal> goal_opt = goalRepository.findById(taskPostDTO.getGoal_id());
            if (goal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!");
            }
            new_task.setGoal(goal_opt.get());
        }
        taskRepository.save(new_task);
        return new MessageResponse("Task added.", MessageType.SUCCESS);
    }

    public MessageResponse createReflection(ReflectionPostDTO reflectionPostDTO) {
        if (reflectionPostDTO.getGoal_id() == null && reflectionPostDTO.getSubgoal_id() == null) {
            return new MessageResponse("This entity must belong to a goal or a subgoal!", MessageType.ERROR);
        }
        Reflection new_reflection = reflectionPostMapper.mapToEntity(reflectionPostDTO);
        new_reflection.setEntitiType(EntitiType.REFLECTION);
        new_reflection.setIsDone(Boolean.FALSE);
        if (reflectionPostDTO.getGoal_id() == null) {
            Optional<Subgoal> subgoal_opt = subgoalRepository.findById(reflectionPostDTO.getSubgoal_id());
            if (subgoal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
            }
            new_reflection.setSubgoal(subgoal_opt.get());
        } else {
            Optional<Goal> goal_opt = goalRepository.findById(reflectionPostDTO.getGoal_id());
            if (goal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!");
            }
            new_reflection.setGoal(goal_opt.get());
        }
        reflectionRepository.save(new_reflection);
        return new MessageResponse("Reflection added.", MessageType.SUCCESS);
    }

    public MessageResponse createQuestion(QuestionPostDTO questionPostDTO) {
        if (questionPostDTO.getGoal_id() == null && questionPostDTO.getSubgoal_id() == null) {
            return new MessageResponse("This entity must belong to a goal or a subgoal!", MessageType.ERROR);
        }
        Question new_question = questionPostMapper.mapToEntity(questionPostDTO);
        new_question.setEntitiType(EntitiType.QUESTION);
        new_question.setIsDone(Boolean.FALSE);
        if (questionPostDTO.getGoal_id() == null) {
            Optional<Subgoal> subgoal_opt = subgoalRepository.findById(questionPostDTO.getSubgoal_id());
            if (subgoal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
            }
            new_question.setSubgoal(subgoal_opt.get());
        } else {
            Optional<Goal> goal_opt = goalRepository.findById(questionPostDTO.getGoal_id());
            if (goal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!");
            }
            new_question.setGoal(goal_opt.get());
        }
        questionRepository.save(new_question);
        return new MessageResponse("Question added.", MessageType.SUCCESS);
    }

    public MessageResponse createRoutine(RoutinePostDTO routinePostDTO) {
        if (routinePostDTO.getGoal_id() == null && routinePostDTO.getSubgoal_id() == null) {
            return new MessageResponse("This entity must belong to a goal or a subgoal!", MessageType.ERROR);
        }
        Routine new_routine = routinePostMapper.mapToEntity(routinePostDTO);
        new_routine.setEntitiType(EntitiType.ROUTINE);
        new_routine.setIsDone(Boolean.FALSE);
        if (routinePostDTO.getGoal_id() == null) {
            Optional<Subgoal> subgoal_opt = subgoalRepository.findById(routinePostDTO.getSubgoal_id());
            if (subgoal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
            }
            new_routine.setSubgoal(subgoal_opt.get());
        } else {
            Optional<Goal> goal_opt = goalRepository.findById(routinePostDTO.getGoal_id());
            if (goal_opt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!");
            }
            new_routine.setGoal(goal_opt.get());
        }
        routineRepository.save(new_routine);
        return new MessageResponse("Routine added.", MessageType.SUCCESS);
    }

    /****************************** GETS ********************************/
    /* I don't think that this endpoint is needed.
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
        return new EntitiDTO();// it wont reach here anyways
    }
    */

    private Set<EntitiDTOShort> extractEntities(Entiti entiti){

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                entiti.getSublinks().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinks().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinks().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinks().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    @Transactional
    public TaskGetDTO getTask(Long id) {
        Optional<Task> task_from_db_opt = taskRepository.findById(id);
        if (task_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!");
        }
        TaskGetDTO taskGetDTO = taskGetMapper.mapToDto(task_from_db_opt.get());
        if (task_from_db_opt.get().getGoal() == null) {
            taskGetDTO.setSubgoal_id(task_from_db_opt.get().getSubgoal().getId());
        } else {
            taskGetDTO.setGoal_id(task_from_db_opt.get().getGoal().getId());
        }

        taskGetDTO.setResources(resourceShortMapper.mapToDto(task_from_db_opt.get().getResources().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        taskGetDTO.setSublinks(extractEntities(task_from_db_opt.get()));
        return taskGetDTO;
    }

    @Transactional
    public RoutineGetDTO getRoutine(Long id) {
        Optional<Routine> routine_from_db_opt = routineRepository.findById(id);
        if (routine_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!");
        }
        RoutineGetDTO routineGetDTO = routineGetMapper.mapToDto(routine_from_db_opt.get());
        if (routine_from_db_opt.get().getGoal() == null) {
            routineGetDTO.setSubgoal_id(routine_from_db_opt.get().getSubgoal().getId());
        } else {
            routineGetDTO.setGoal_id(routine_from_db_opt.get().getGoal().getId());
        }
        routineGetDTO.setResources(resourceShortMapper.mapToDto(routine_from_db_opt.get().getResources().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        routineGetDTO.setSublinks(extractEntities(routine_from_db_opt.get()));
        return routineGetDTO;
    }

    @Transactional
    public ReflectionGetDTO getReflection(Long id) {
        Optional<Reflection> reflection_from_db_opt = reflectionRepository.findById(id);
        if (reflection_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!");
        }
        ReflectionGetDTO reflectionGetDTO = reflectionGetMapper.mapToDto(reflection_from_db_opt.get());
        if (reflection_from_db_opt.get().getGoal() == null) {
            reflectionGetDTO.setSubgoal_id(reflection_from_db_opt.get().getSubgoal().getId());
        } else {
            reflectionGetDTO.setGoal_id(reflection_from_db_opt.get().getGoal().getId());
        }
        reflectionGetDTO.setResources(resourceShortMapper.mapToDto(reflection_from_db_opt.get().getResources().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        reflectionGetDTO.setSublinks(extractEntities(reflection_from_db_opt.get()));
        return reflectionGetDTO;
    }

    @Transactional
    public QuestionGetDTO getQuestion(Long id) {
        Optional<Question> question_from_db_opt = questionRepository.findById(id);
        if (question_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!");
        }
        QuestionGetDTO questionGetDTO = questionGetMapper.mapToDto(question_from_db_opt.get());
        if (question_from_db_opt.get().getGoal() == null) {
            questionGetDTO.setSubgoal_id(question_from_db_opt.get().getSubgoal().getId());
        } else {
            questionGetDTO.setGoal_id(question_from_db_opt.get().getGoal().getId());
        }
        questionGetDTO.setResources(resourceShortMapper.mapToDto(question_from_db_opt.get().getResources().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        questionGetDTO.setSublinks(extractEntities(question_from_db_opt.get()));
        return questionGetDTO;
    }

    /****************************** DELETES ********************************/

    public MessageResponse deleteReflection(Long id) {
        Optional<Reflection> reflection_from_db_opt = reflectionRepository.findById(id);
        if (reflection_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!");
        }
        if(reflection_from_db_opt.get().getGoal() == null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = reflection_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(reflection_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            reflection_from_db_opt.get().setSubgoal(null);
        }else{
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = reflection_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(reflection_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            reflection_from_db_opt.get().setGoal(null);
        }
        resourceRepository.deleteAll(reflection_from_db_opt.get().getResources());
        reflectionRepository.deleteById(id);
        return new MessageResponse("Reflection deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteQuestion(Long id) {
        Optional<Question> question_from_db_opt = questionRepository.findById(id);
        if (question_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!");
        }
        if(question_from_db_opt.get().getGoal() == null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = question_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(question_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            question_from_db_opt.get().setSubgoal(null);
        }else{
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = question_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(question_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            question_from_db_opt.get().setGoal(null);
        }
        resourceRepository.deleteAll(question_from_db_opt.get().getResources());
        questionRepository.deleteById(id);
        return new MessageResponse("Question deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteTask(Long id) {
        Optional<Task> task_from_db_opt = taskRepository.findById(id);
        if (task_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!");
        }
        if(task_from_db_opt.get().getGoal() == null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = task_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(task_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            task_from_db_opt.get().setSubgoal(null);
        }else{
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = task_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(task_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            task_from_db_opt.get().setGoal(null);
        }
        resourceRepository.deleteAll(task_from_db_opt.get().getResources());
        taskRepository.deleteById(id);
        return new MessageResponse("Task deleted!", MessageType.SUCCESS);
    }

    public MessageResponse deleteRoutine(Long id) {
        Optional<Routine> routine_from_db_opt = routineRepository.findById(id);
        if (routine_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!");
        }
        if(routine_from_db_opt.get().getGoal() == null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = routine_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(routine_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            routine_from_db_opt.get().setSubgoal(null);
        }else{
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = routine_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(routine_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            routine_from_db_opt.get().setGoal(null);
        }
        resourceRepository.deleteAll(routine_from_db_opt.get().getResources());
        routineRepository.deleteById(id);
        return new MessageResponse("Routine deleted!", MessageType.SUCCESS);
    }

    /****************************** PUTS ********************************/

    public MessageResponse updateTask(TaskGetDTO task_dto) {
        Optional<Task> task_from_db_opt = taskRepository.findById(task_dto.getId());
        if (task_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!");
        }

        if (task_dto.getDeadline() != null)
            task_from_db_opt.get().setDeadline(task_dto.getDeadline());
        if (task_dto.getDescription() != null)
            task_from_db_opt.get().setDescription(task_dto.getDescription());
        if (task_dto.getTitle() != null)
            task_from_db_opt.get().setTitle(task_dto.getTitle());
        if (task_dto.getIsDone() != null)
            task_from_db_opt.get().setIsDone(task_dto.getIsDone());

        taskRepository.save(task_from_db_opt.get());
        return new MessageResponse("Updated task", MessageType.SUCCESS);
    }

    public MessageResponse updateRoutine(RoutineGetDTO routine_dto) {
        Optional<Routine> routine_from_db_opt = routineRepository.findById(routine_dto.getId());
        if (routine_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!");
        }

        if (routine_dto.getPeriod() != null)
            routine_from_db_opt.get().setPeriod(routine_dto.getPeriod());
        if (routine_dto.getDeadline() != null)
            routine_from_db_opt.get().setDeadline(routine_dto.getDeadline());
        if (routine_dto.getDescription() != null)
            routine_from_db_opt.get().setDescription(routine_dto.getDescription());
        if (routine_dto.getTitle() != null)
            routine_from_db_opt.get().setTitle(routine_dto.getTitle());
        if (routine_dto.getIsDone() != null)
            routine_from_db_opt.get().setIsDone(routine_dto.getIsDone());

        routineRepository.save(routine_from_db_opt.get());
        return new MessageResponse("Updated routine", MessageType.SUCCESS);
    }

    public MessageResponse updateReflection(ReflectionGetDTO reflection_dto) {
        Optional<Reflection> reflection_from_db_opt = reflectionRepository.findById(reflection_dto.getId());
        if (reflection_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!");
        }
        if (reflection_dto.getDescription() != null)
            reflection_from_db_opt.get().setDescription(reflection_dto.getDescription());
        if (reflection_dto.getTitle() != null)
            reflection_from_db_opt.get().setTitle(reflection_dto.getTitle());
        if (reflection_dto.getIsDone() != null)
            reflection_from_db_opt.get().setIsDone(reflection_dto.getIsDone());
        reflectionRepository.save(reflection_from_db_opt.get());
        return new MessageResponse("Updated reflection", MessageType.SUCCESS);

    }

    public MessageResponse updateQuestion(QuestionGetDTO question_dto) {
        Optional<Question> question_from_db_opt = questionRepository.findById(question_dto.getId());
        if (question_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!");
        }
        if (question_dto.getDescription() != null)
            question_from_db_opt.get().setDescription(question_dto.getDescription());
        if (question_dto.getTitle() != null)
            question_from_db_opt.get().setTitle(question_dto.getTitle());
        if (question_dto.getIsDone() != null)
            question_from_db_opt.get().setIsDone(question_dto.getIsDone());
        questionRepository.save(question_from_db_opt.get());
        return new MessageResponse("Updated question", MessageType.SUCCESS);

    }

}
