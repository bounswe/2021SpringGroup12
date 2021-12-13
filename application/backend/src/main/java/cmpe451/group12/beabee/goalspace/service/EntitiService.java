package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.*;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.mapper.entities.*;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EntitiService {

    private final GoalRepository goalRepository;
    private final GroupGoalRepository groupGoalRepository;
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


    public List<EntitiDTOShort> getEntitiesOfAGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        List<Subgoal> subgoals = goal_from_db.getSubgoals().stream()
                .flatMap(EntitiService::recursiveSubgoals).collect(Collectors.toList());
        List<Entiti> all_entities = subgoals.stream().map(x -> x.getEntities()).flatMap(Set::stream).collect(Collectors.toList());
        all_entities.addAll(goal_from_db.getEntities());
        List<EntitiDTOShort> result = new ArrayList<>();
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        return  result;
    }

    private static Stream<Subgoal> recursiveSubgoals(Subgoal item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(EntitiService::recursiveSubgoals));
    }


    public List<EntitiDTOShort> getEntitiesOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        Users user = userRepository.getById(user_id);
        List<Entiti> entities = entitiRepository.findAllByCreator(user);
        List<EntitiDTOShort> entity_dtos = new ArrayList<>();
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        entity_dtos.addAll(entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        return entity_dtos;
    }

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

    public MessageResponse createReflection(ReflectionPostDTO reflectionPostDTO) {
        Reflection new_reflection = reflectionPostMapper.mapToEntity(reflectionPostDTO);
        new_reflection.setEntitiType(EntitiType.REFLECTION);
        new_reflection.setIsDone(Boolean.FALSE);
        if (reflectionPostDTO.getParentType().equals(ParentType.GOAL)){
            Goal goal = goalRepository.findById(reflectionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
            new_reflection.setGoal(goal);
            new_reflection.setCreator(goal.getCreator());
        }
        if (reflectionPostDTO.getParentType().equals(ParentType.ENTITY)){
            Entiti entiti = entitiRepository.findById(reflectionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));
            Set<Entiti> sublinks = entiti.getSublinks();
            sublinks.add(new_reflection);
            entiti.setSublinks(sublinks);
            new_reflection.setCreator(entiti.getCreator());
        }
        if (reflectionPostDTO.getParentType().equals(ParentType.SUBGOAL)){
            Subgoal subgoal = subgoalRepository.findById(reflectionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));
            new_reflection.setSubgoal(subgoal);
            new_reflection.setCreator(subgoal.getCreator());
        }
        if (reflectionPostDTO.getParentType().equals(ParentType.GROUPGOAL)){
            GroupGoal groupGoal = groupGoalRepository.findById(reflectionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
            new_reflection.setGroupgoal(groupGoal);
            new_reflection.setCreator(groupGoal.getCreator());
        }
        if (reflectionPostDTO.getParentType().equals(ParentType.NONE)){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            Users user = userRepository.findByUsername(username).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN, "User could not authenticated!"));
            new_reflection.setCreator(user);
        }

        reflectionRepository.save(new_reflection);
        return new MessageResponse("Reflection added.", MessageType.SUCCESS);
    }

    public MessageResponse createTask(TaskPostDTO taskPostDTO) {
        Task new_task = taskPostMapper.mapToEntity(taskPostDTO);
        // dummy change to make this branch different than dev/backend
        new_task.setEntitiType(EntitiType.TASK);
        new_task.setIsDone(Boolean.FALSE);
        new_task.setExtension_count(0L);
        if (taskPostDTO.getParentType().equals(ParentType.GOAL)){
            Goal goal = goalRepository.findById(taskPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
            new_task.setGoal(goal);
            new_task.setCreator(goal.getCreator());
        }
        if (taskPostDTO.getParentType().equals(ParentType.ENTITY)){
            Entiti entiti = entitiRepository.findById(taskPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));
            Set<Entiti> sublinks = entiti.getSublinks();
            sublinks.add(new_task);
            entiti.setSublinks(sublinks);
            new_task.setCreator(entiti.getCreator());
        }
        if (taskPostDTO.getParentType().equals(ParentType.SUBGOAL)){
            Subgoal subgoal = subgoalRepository.findById(taskPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));
            new_task.setSubgoal(subgoal);
            new_task.setCreator(subgoal.getCreator());
        }
        if (taskPostDTO.getParentType().equals(ParentType.GROUPGOAL)){
            GroupGoal groupGoal = groupGoalRepository.findById(taskPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
            new_task.setGroupgoal(groupGoal);
            new_task.setCreator(groupGoal.getCreator());
        }
        if (taskPostDTO.getParentType().equals(ParentType.NONE)){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            Users user = userRepository.findByUsername(username).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN, "User could not authenticated!"));
            new_task.setCreator(user);
        }

        taskRepository.save(new_task);
        return new MessageResponse("Task added.", MessageType.SUCCESS);
    }

    public MessageResponse createQuestion(QuestionPostDTO questionPostDTO) {
        Question new_question = questionPostMapper.mapToEntity(questionPostDTO);
        new_question.setEntitiType(EntitiType.QUESTION);
        new_question.setIsDone(Boolean.FALSE);
        if (questionPostDTO.getParentType().equals(ParentType.GOAL)){
            Goal goal = goalRepository.findById(questionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
            new_question.setGoal(goal);
            new_question.setCreator(goal.getCreator());
        }
        if (questionPostDTO.getParentType().equals(ParentType.ENTITY)){
            Entiti entiti = entitiRepository.findById(questionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));
            Set<Entiti> sublinks = entiti.getSublinks();
            sublinks.add(new_question);
            entiti.setSublinks(sublinks);
            new_question.setCreator(entiti.getCreator());
        }
        if (questionPostDTO.getParentType().equals(ParentType.SUBGOAL)){
            Subgoal subgoal = subgoalRepository.findById(questionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));
            new_question.setSubgoal(subgoal);
            new_question.setCreator(subgoal.getCreator());
        }
        if (questionPostDTO.getParentType().equals(ParentType.GROUPGOAL)){
            GroupGoal groupGoal = groupGoalRepository.findById(questionPostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
            new_question.setGroupgoal(groupGoal);
            new_question.setCreator(groupGoal.getCreator());
        }
        if (questionPostDTO.getParentType().equals(ParentType.NONE)){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            Users user = userRepository.findByUsername(username).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN, "User could not authenticated!"));
            new_question.setCreator(user);
        }

        questionRepository.save(new_question);
        return new MessageResponse("Question added.", MessageType.SUCCESS);
    }

    public MessageResponse createRoutine(RoutinePostDTO routinePostDTO) {
        Routine new_routine = routinePostMapper.mapToEntity(routinePostDTO);
        new_routine.setEntitiType(EntitiType.ROUTINE);
        new_routine.setIsDone(Boolean.FALSE);
        new_routine.setExtension_count(0L);
        if (routinePostDTO.getParentType().equals(ParentType.GOAL)){
            Goal goal = goalRepository.findById(routinePostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
            new_routine.setGoal(goal);
            new_routine.setCreator(goal.getCreator());
        }
        if (routinePostDTO.getParentType().equals(ParentType.ENTITY)){
            Entiti entiti = entitiRepository.findById(routinePostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));
            Set<Entiti> sublinks = entiti.getSublinks();
            sublinks.add(new_routine);
            entiti.setSublinks(sublinks);
            new_routine.setCreator(entiti.getCreator());
        }
        if (routinePostDTO.getParentType().equals(ParentType.SUBGOAL)){
            Subgoal subgoal = subgoalRepository.findById(routinePostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));
            new_routine.setSubgoal(subgoal);
            new_routine.setCreator(subgoal.getCreator());
        }
        if (routinePostDTO.getParentType().equals(ParentType.GROUPGOAL)){
            GroupGoal groupGoal = groupGoalRepository.findById(routinePostDTO.getParent_id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
            new_routine.setGroupgoal(groupGoal);
            new_routine.setCreator(groupGoal.getCreator());
        }
        if (routinePostDTO.getParentType().equals(ParentType.NONE)){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            } else {
                username = principal.toString();
            }
            Users user = userRepository.findByUsername(username).orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN, "User could not authenticated!"));
            new_routine.setCreator(user);
        }

        routineRepository.save(new_routine);
        return new MessageResponse("Routine added.", MessageType.SUCCESS);
    }

    /****************************** GETS ********************************/

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
        if (task_from_db_opt.get().getSubgoal() != null) {
            taskGetDTO.setSubgoal_id(task_from_db_opt.get().getSubgoal().getId());
        } else if (task_from_db_opt.get().getGoal() != null) {
            taskGetDTO.setGoal_id(task_from_db_opt.get().getGoal().getId());
        } else if (task_from_db_opt.get().getGroupgoal() != null) {
            taskGetDTO.setGoal_id(task_from_db_opt.get().getGroupgoal().getId());
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
        if (routine_from_db_opt.get().getSubgoal() != null) {
            routineGetDTO.setSubgoal_id(routine_from_db_opt.get().getSubgoal().getId());
        } else if (routine_from_db_opt.get().getGoal() != null) {
            routineGetDTO.setGoal_id(routine_from_db_opt.get().getGoal().getId());
        } else if (routine_from_db_opt.get().getGroupgoal() != null) {
            routineGetDTO.setGoal_id(routine_from_db_opt.get().getGroupgoal().getId());
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
        if (reflection_from_db_opt.get().getSubgoal() != null) {
            reflectionGetDTO.setSubgoal_id(reflection_from_db_opt.get().getSubgoal().getId());
        } else if (reflection_from_db_opt.get().getGoal() != null) {
            reflectionGetDTO.setGoal_id(reflection_from_db_opt.get().getGoal().getId());
        } else if (reflection_from_db_opt.get().getGroupgoal() != null) {
            reflectionGetDTO.setGoal_id(reflection_from_db_opt.get().getGroupgoal().getId());
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
        if (question_from_db_opt.get().getSubgoal() != null) {
            questionGetDTO.setSubgoal_id(question_from_db_opt.get().getSubgoal().getId());
        } else if (question_from_db_opt.get().getGoal() != null) {
            questionGetDTO.setGoal_id(question_from_db_opt.get().getGoal().getId());
        } else if (question_from_db_opt.get().getGroupgoal() != null) {
            questionGetDTO.setGoal_id(question_from_db_opt.get().getGroupgoal().getId());
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
        if(reflection_from_db_opt.get().getSubgoal() != null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = reflection_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(reflection_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            reflection_from_db_opt.get().setSubgoal(null);
        }else if(reflection_from_db_opt.get().getGoal() != null){
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = reflection_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(reflection_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            reflection_from_db_opt.get().setGoal(null);
        }else if(reflection_from_db_opt.get().getGroupgoal() != null){
            //this entiti belongs to a group goal, not a subgoal or goal
            //remove this from group goal's entities
            GroupGoal main_Groupgoal = reflection_from_db_opt.get().getGroupgoal();
            Set<Entiti> entities = main_Groupgoal.getEntities();
            entities.remove(reflection_from_db_opt.get());
            main_Groupgoal.setEntities(entities);
            groupGoalRepository.save(main_Groupgoal);
            reflection_from_db_opt.get().setGroupgoal(null);
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
        if(question_from_db_opt.get().getSubgoal() != null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = question_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(question_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            question_from_db_opt.get().setSubgoal(null);
        }else if(question_from_db_opt.get().getGoal() != null){
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = question_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(question_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            question_from_db_opt.get().setGoal(null);
        }else if(question_from_db_opt.get().getGroupgoal() != null){
            //this entiti belongs to a group goal, not a subgoal or goal
            //remove this from group goal's entities
            GroupGoal main_Groupgoal = question_from_db_opt.get().getGroupgoal();
            Set<Entiti> entities = main_Groupgoal.getEntities();
            entities.remove(question_from_db_opt.get());
            main_Groupgoal.setEntities(entities);
            groupGoalRepository.save(main_Groupgoal);
            question_from_db_opt.get().setGroupgoal(null);
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
        if(task_from_db_opt.get().getSubgoal() != null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = task_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(task_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            task_from_db_opt.get().setSubgoal(null);
        }else if(task_from_db_opt.get().getGoal() != null){
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = task_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(task_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            task_from_db_opt.get().setGoal(null);
        }else if(task_from_db_opt.get().getGroupgoal() != null){
            //this entiti belongs to a group goal, not a subgoal or goal
            //remove this from group goal's entities
            GroupGoal main_Groupgoal = task_from_db_opt.get().getGroupgoal();
            Set<Entiti> entities = main_Groupgoal.getEntities();
            entities.remove(task_from_db_opt.get());
            main_Groupgoal.setEntities(entities);
            groupGoalRepository.save(main_Groupgoal);
            task_from_db_opt.get().setGroupgoal(null);
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
        if(routine_from_db_opt.get().getSubgoal() != null){
            //this entiti belongs to a subgoal, not a goal
            Subgoal subgoal = routine_from_db_opt.get().getSubgoal();
            Set<Entiti> entities = subgoal.getEntities();
            entities.remove(routine_from_db_opt.get());
            subgoal.setEntities(entities);
            subgoalRepository.save(subgoal);
            routine_from_db_opt.get().setSubgoal(null);
        }else if(routine_from_db_opt.get().getGoal() != null){
            //this entiti belongs to a goal, not a subgoal
            //remove this from goal's entities
            Goal main_goal = routine_from_db_opt.get().getGoal();
            Set<Entiti> entities = main_goal.getEntities();
            entities.remove(routine_from_db_opt.get());
            main_goal.setEntities(entities);
            goalRepository.save(main_goal);
            routine_from_db_opt.get().setGoal(null);
        }else if(routine_from_db_opt.get().getGroupgoal() != null){
            //this entiti belongs to a group goal, not a subgoal or goal
            //remove this from group goal's entities
            GroupGoal main_Groupgoal = routine_from_db_opt.get().getGroupgoal();
            Set<Entiti> entities = main_Groupgoal.getEntities();
            entities.remove(routine_from_db_opt.get());
            main_Groupgoal.setEntities(entities);
            groupGoalRepository.save(main_Groupgoal);
            routine_from_db_opt.get().setGroupgoal(null);
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


    /************************************ EXTEND ********************************/
    public MessageResponse extendEntiti(Long entiti_id, Date newDeadline) {
        Entiti entiti_from_db = entitiRepository.findById(entiti_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found!"));

        if(entiti_from_db.getEntitiType().equals(EntitiType.ROUTINE)){
            Routine routine = (Routine) entiti_from_db;
            if (newDeadline.compareTo(routine.getDeadline().get(routine.getDeadline().size()-1)) <= 0 ) {
                return new MessageResponse("New deadline must be later than current deadline!", MessageType.ERROR);
            }
            List<Date> deadlines = routine.getDeadline();
            deadlines.add(newDeadline);
            deadlines.remove(deadlines.get(deadlines.size()-2));
            routine.setDeadline(deadlines);
            routine.setExtension_count(routine.getExtension_count() + 1);
            entitiRepository.save(routine);
        }
        if(entiti_from_db.getEntitiType().equals(EntitiType.TASK)){
            Task task = (Task) entiti_from_db;
            if (newDeadline.compareTo(task.getDeadline()) <= 0 ) {
                return new MessageResponse("New deadline must be later than current deadline!", MessageType.ERROR);
            }
            task.setDeadline(newDeadline);
            task.setExtension_count(task.getExtension_count() + 1);
            entitiRepository.save(task);
        }
        return new MessageResponse("Entity extended successfully!", MessageType.SUCCESS);
    }

    /********************************** ROUTINE RATE *****************/
    public MessageResponse rateRoutine(Long routine_id, Long rating){
        Routine routine_from_db = routineRepository.findById(routine_id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!"));
        List<Double> ratings = routine_from_db.getRating();
        ratings.add(rating.doubleValue());
        routine_from_db.setRating(ratings);

        List<Date> deadlines = routine_from_db.getDeadline();
        System.out.println(routine_from_db.getDeadline().size());
        deadlines.add(new Date(routine_from_db.getDeadline().get(routine_from_db.getDeadline().size()-1).getTime() +  routine_from_db.getPeriod() *(1000*60*60*24)));
        routine_from_db.setDeadline(deadlines);

        routineRepository.save(routine_from_db);

        return new MessageResponse("This deadline evaluated successfully, move on to next deadline!",MessageType.SUCCESS);

    }
}
