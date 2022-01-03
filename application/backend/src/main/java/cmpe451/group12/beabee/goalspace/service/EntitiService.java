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
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.resources.ResourceDTOShort;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.mapper.entities.*;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
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
    private final EntitiShortMapper entitiShortMapper;
    private final EntitiRepository entitiRepository;
    private final SubgoalShortMapper subgoalShortMapper;
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
        List<Entiti> all_entities = new ArrayList<>(goal_from_db.getEntities());

        List<EntitiDTOShort> result = new ArrayList<>();
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Question")).map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Task")).map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Reflection")).map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toList()));
        result.addAll(all_entities.stream().filter(x -> x.getClass().getSimpleName().equals("Routine")).map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toList()));
        return  result;
    }


    public List<EntitiDTOShort> getEntitiesOfAGroupgoal(Long groupgoal_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        List<Entiti> all_entities = new ArrayList<>(groupGoal.getEntities());

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

    private Long parentOfSubgoal(Subgoal subgoal) {
        if (subgoal.getMainGroupgoal() != null)
            return subgoal.getMainGroupgoal().getId();
        else if (subgoal.getMainGoal() != null)
            return subgoal.getMainGoal().getId();
        return parentOfSubgoal(subgoalRepository.findParentById(subgoal.getId()));
    }

    public MessageResponse entitiLink(Long id, EntitiLinkDTO entitiLinkDTO) {
        Entiti parentEntiti = entitiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found"));

        // Check the type of the object we want to link
        switch (entitiLinkDTO.getChildType()) {
            case ENTITI:
                Entiti childEntiti = entitiRepository.findById(entitiLinkDTO.getChildId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child entity not found"));

                if(parentEntiti.getGoal() != childEntiti.getGoal() || parentEntiti.getGroupgoal() != childEntiti.getGroupgoal())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Child entity is not in the same group!");

                parentEntiti.getSublinked_entities().add(childEntiti);
                break;

            case SUBGOAL:
                Subgoal childSubgoal = subgoalRepository.findById(entitiLinkDTO.getChildId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child subgoal not found"));

                if(!parentEntiti.getGoal().getId().equals(parentOfSubgoal(childSubgoal)) &&
                        !parentEntiti.getGroupgoal().getId().equals(parentOfSubgoal(childSubgoal)))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Child subgoal is not in the same group!");

                parentEntiti.getSublinked_subgoals().add(childSubgoal);
                break;
            case GOAL:
            case GROUPGOAL:
                if(parentEntiti.getIsLinkedToGoal())
                    return new MessageResponse("Already linked to parent", MessageType.ERROR);
                parentEntiti.setIsLinkedToGoal(Boolean.TRUE);
                break;
        }

        entitiRepository.save(parentEntiti);
        return new MessageResponse("Linking operation is successful.", MessageType.SUCCESS);
    }

    public MessageResponse entitiDeleteLink(Long id, EntitiLinkDTO entitiLinkDTO) {
        Entiti parentEntiti = entitiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found"));

        // Check the type of the object we want to link
        switch (entitiLinkDTO.getChildType()) {
            case ENTITI:
                Entiti childEntiti = entitiRepository.findById(entitiLinkDTO.getChildId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child entity not found"));
                if(!parentEntiti.getSublinked_entities().removeIf(e -> e.equals(childEntiti)))
                    return new MessageResponse("Link does not exist", MessageType.ERROR);
                break;

            case SUBGOAL:
                Subgoal childSubgoal = subgoalRepository.findById(entitiLinkDTO.getChildId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Child subgoal not found"));
                if(!parentEntiti.getSublinked_subgoals().removeIf(e -> e.equals(childSubgoal)))
                    return new MessageResponse("Link does not exist", MessageType.ERROR);
                break;
            case GOAL:
            case GROUPGOAL:
                if(!parentEntiti.getIsLinkedToGoal())
                    return new MessageResponse("Link does not exist", MessageType.ERROR);
                parentEntiti.setIsLinkedToGoal(Boolean.FALSE);
                break;
        }
        entitiRepository.save(parentEntiti);
        return new MessageResponse("Link deleted is successful.", MessageType.SUCCESS);
    }

    /****************************** POSTS ********************************/

    public MessageResponse createReflection(ReflectionPostDTO reflectionPostDTO) {
        Reflection new_reflection = reflectionPostMapper.mapToEntity(reflectionPostDTO);
        new_reflection.setEntitiType(EntitiType.REFLECTION);
        new_reflection.setIsDone(Boolean.FALSE);
        new_reflection.setIsLinkedToGoal(Boolean.TRUE);

        switch (reflectionPostDTO.getGoalType()) {
            case GOAL:
                Goal goal = goalRepository.findById(reflectionPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
                new_reflection.setGoal(goal);
                new_reflection.setCreator(goal.getCreator());
                break;
            case GROUPGOAL:
                GroupGoal groupGoal = groupGoalRepository.findById(reflectionPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
                new_reflection.setGroupgoal(groupGoal);
                new_reflection.setCreator(groupGoal.getCreator());
                break;
        }

        if (reflectionPostDTO.getInitialLinkType() != null && !(reflectionPostDTO.getInitialParentId() < 0)) {
            switch (reflectionPostDTO.getInitialLinkType())
            {
                case ENTITI:
                    Entiti entiti = entitiRepository.findById(reflectionPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));

                    if(new_reflection.getGoal() != entiti.getGoal() || new_reflection.getGroupgoal() != entiti.getGroupgoal())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent entity is not in the same group!");

                    new_reflection.setCreator(entiti.getCreator());
                    new_reflection.setIsLinkedToGoal(Boolean.FALSE);
                    Reflection saved_reflection = reflectionRepository.save(new_reflection);
                    entiti.getSublinked_entities().add(saved_reflection);
                    entitiRepository.save(entiti);
                    break;
                case SUBGOAL:
                    Subgoal subgoal = subgoalRepository.findById(reflectionPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));

                    if(!new_reflection.getGoal().getId().equals(parentOfSubgoal(subgoal)) &&
                            !new_reflection.getGroupgoal().getId().equals(parentOfSubgoal(subgoal)))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent subgoal is not in the same group!");

                    new_reflection.setSublinked_subgoals(Set.of(subgoal));
                    new_reflection.setCreator(subgoal.getCreator());
                    new_reflection.setIsLinkedToGoal(Boolean.FALSE);
                    reflectionRepository.save(new_reflection);
                    break;
                case GOAL:
                case GROUPGOAL:
                    reflectionRepository.save(new_reflection);
                    break;
            }
        }
        else
            reflectionRepository.save(new_reflection);

        return new MessageResponse("Reflection added.", MessageType.SUCCESS);
    }

    public MessageResponse createTask(TaskPostDTO taskPostDTO) {
        Task new_task = taskPostMapper.mapToEntity(taskPostDTO);
        // dummy change to make this branch different than dev/backend
        new_task.setEntitiType(EntitiType.TASK);
        new_task.setIsDone(Boolean.FALSE);
        new_task.setExtension_count(0L);
        new_task.setIsLinkedToGoal(Boolean.TRUE);

        switch (taskPostDTO.getGoalType()) {
            case GOAL:
                Goal goal = goalRepository.findById(taskPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
                new_task.setGoal(goal);
                new_task.setCreator(goal.getCreator());
                break;
            case GROUPGOAL:
                GroupGoal groupGoal = groupGoalRepository.findById(taskPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
                new_task.setGroupgoal(groupGoal);
                new_task.setCreator(groupGoal.getCreator());
                break;
        }

        if (taskPostDTO.getInitialLinkType() != null && !(taskPostDTO.getInitialParentId() < 0)) {
            switch (taskPostDTO.getInitialLinkType())
            {
                case ENTITI:
                    Entiti entiti = entitiRepository.findById(taskPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));

                    if(new_task.getGoal() != entiti.getGoal() || new_task.getGroupgoal() != entiti.getGroupgoal())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent entity is not in the same group!");

                    new_task.setCreator(entiti.getCreator());
                    new_task.setIsLinkedToGoal(Boolean.FALSE);
                    Task saved_task = taskRepository.save(new_task);
                    entiti.getSublinked_entities().add(saved_task);
                    entitiRepository.save(entiti);
                    break;
                case SUBGOAL:
                    Subgoal subgoal = subgoalRepository.findById(taskPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));

                    if(!new_task.getGoal().getId().equals(parentOfSubgoal(subgoal)) &&
                            !new_task.getGroupgoal().getId().equals(parentOfSubgoal(subgoal)))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent subgoal is not in the same group!");

                    new_task.setSublinked_subgoals(Set.of(subgoal));
                    new_task.setCreator(subgoal.getCreator());
                    new_task.setIsLinkedToGoal(Boolean.FALSE);
                    taskRepository.save(new_task);
                    break;
                case GOAL:
                case GROUPGOAL:
                    taskRepository.save(new_task);
                    break;
            }
        }
        else
            taskRepository.save(new_task);


        return new MessageResponse("Task added.", MessageType.SUCCESS);
    }

    public MessageResponse createQuestion(QuestionPostDTO questionPostDTO) {
        Question new_question = questionPostMapper.mapToEntity(questionPostDTO);
        new_question.setEntitiType(EntitiType.QUESTION);
        new_question.setIsDone(Boolean.FALSE);
        new_question.setIsLinkedToGoal(Boolean.TRUE);


        switch (questionPostDTO.getGoalType()) {
            case GOAL:
                Goal goal = goalRepository.findById(questionPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
                new_question.setGoal(goal);
                new_question.setCreator(goal.getCreator());
                break;
            case GROUPGOAL:
                GroupGoal groupGoal = groupGoalRepository.findById(questionPostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
                new_question.setGroupgoal(groupGoal);
                new_question.setCreator(groupGoal.getCreator());
                break;
        }

        if (questionPostDTO.getInitialLinkType() != null && !(questionPostDTO.getInitialParentId() < 0)) {
            switch (questionPostDTO.getInitialLinkType())
            {
                case ENTITI:
                    Entiti entiti = entitiRepository.findById(questionPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));

                    if(new_question.getGoal() != entiti.getGoal() || new_question.getGroupgoal() != entiti.getGroupgoal())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent entity is not in the same group!");

                    new_question.setCreator(entiti.getCreator());
                    new_question.setIsLinkedToGoal(Boolean.FALSE);
                    Question saved_question = questionRepository.save(new_question);
                    entiti.getSublinked_entities().add(saved_question);
                    entitiRepository.save(saved_question);
                    break;
                case SUBGOAL:
                    Subgoal subgoal = subgoalRepository.findById(questionPostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));

                    if(!new_question.getGoal().getId().equals(parentOfSubgoal(subgoal)) &&
                            !new_question.getGroupgoal().getId().equals(parentOfSubgoal(subgoal)))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent subgoal is not in the same group!");

                    new_question.setSublinked_subgoals(Set.of(subgoal));
                    new_question.setCreator(subgoal.getCreator());
                    new_question.setIsLinkedToGoal(Boolean.FALSE);
                    questionRepository.save(new_question);
                    break;
                case GOAL:
                case GROUPGOAL:
                    questionRepository.save(new_question);
                    break;
            }
        }
        else
            questionRepository.save(new_question);

        return new MessageResponse("Question added.", MessageType.SUCCESS);
    }

    public MessageResponse createRoutine(RoutinePostDTO routinePostDTO) {
        Routine new_routine = routinePostMapper.mapToEntity(routinePostDTO);
        new_routine.setEntitiType(EntitiType.ROUTINE);
        new_routine.setIsDone(Boolean.FALSE);
        new_routine.setExtension_count(0L);
        new_routine.setIsLinkedToGoal(Boolean.TRUE);

        switch (routinePostDTO.getGoalType()) {
            case GOAL:
                Goal goal = goalRepository.findById(routinePostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent goal not found!"));
                new_routine.setGoal(goal);
                new_routine.setCreator(goal.getCreator());
                break;
            case GROUPGOAL:
                GroupGoal groupGoal = groupGoalRepository.findById(routinePostDTO.getGoalId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent group goal not found!"));
                new_routine.setGroupgoal(groupGoal);
                new_routine.setCreator(groupGoal.getCreator());
                break;
        }

        if (routinePostDTO.getInitialLinkType() != null && !(routinePostDTO.getInitialParentId() < 0)) {
            switch (routinePostDTO.getInitialLinkType())
            {
                case ENTITI:
                    Entiti entiti = entitiRepository.findById(routinePostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent entity not found!"));

                    if(new_routine.getGoal() != entiti.getGoal() || new_routine.getGroupgoal() != entiti.getGroupgoal())
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent entity is not in the same group!");

                    new_routine.setCreator(entiti.getCreator());
                    new_routine.setSublinked_entities(Set.of(entiti));
                    new_routine.setIsLinkedToGoal(Boolean.FALSE);
                    Routine saved_routine = routineRepository.save(new_routine);
                    entiti.getSublinked_entities().add(saved_routine);
                    entitiRepository.save(entiti);
                    break;
                case SUBGOAL:
                    Subgoal subgoal = subgoalRepository.findById(routinePostDTO.getInitialParentId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!"));

                    if(!new_routine.getGoal().getId().equals(parentOfSubgoal(subgoal)) &&
                            !new_routine.getGroupgoal().getId().equals(parentOfSubgoal(subgoal)))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent subgoal is not in the same group!");

                    new_routine.setSublinked_subgoals(Set.of(subgoal));
                    new_routine.setCreator(subgoal.getCreator());
                    new_routine.setIsLinkedToGoal(Boolean.FALSE);
                    routineRepository.save(new_routine);
                    break;
                case GOAL:
                case GROUPGOAL:
                    routineRepository.save(new_routine);
                    break;
            }
        }
        else
            routineRepository.save(new_routine);

        return new MessageResponse("Routine added.", MessageType.SUCCESS);
    }

    /****************************** GETS ********************************/

    private Set<EntitiDTOShort> extractEntities(Entiti entiti){
        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                entiti.getSublinked_entities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinked_entities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinked_entities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                entiti.getSublinked_entities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
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
        if (task_from_db_opt.get().getGoal() != null) {
            taskGetDTO.setGoal_id(task_from_db_opt.get().getGoal().getId());
        } else if (task_from_db_opt.get().getGroupgoal() != null) {
            taskGetDTO.setGoal_id(task_from_db_opt.get().getGroupgoal().getId());
        }

        taskGetDTO.setResources(new HashSet<>(resourceShortMapper.mapToDto(new ArrayList<>(task_from_db_opt.get().getResources()))));
        taskGetDTO.setSublinked_entities(extractEntities(task_from_db_opt.get()));
        taskGetDTO.setSublinked_subgoals(new HashSet<>(
                subgoalShortMapper.mapToDto(new ArrayList<>(task_from_db_opt.get().getSublinked_subgoals())))
        );
        return taskGetDTO;
    }

    @Transactional
    public RoutineGetDTO getRoutine(Long id) {
        Optional<Routine> routine_from_db_opt = routineRepository.findById(id);
        if (routine_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!");
        }
        RoutineGetDTO routineGetDTO = routineGetMapper.mapToDto(routine_from_db_opt.get());
        if (routine_from_db_opt.get().getGoal() != null) {
            routineGetDTO.setGoal_id(routine_from_db_opt.get().getGoal().getId());
        } else if (routine_from_db_opt.get().getGroupgoal() != null) {
            routineGetDTO.setGoal_id(routine_from_db_opt.get().getGroupgoal().getId());
        }
        routineGetDTO.setResources(new HashSet<>(resourceShortMapper.mapToDto(new ArrayList<>(routine_from_db_opt.get().getResources()))));
        routineGetDTO.setSublinked_entities(extractEntities(routine_from_db_opt.get()));
        routineGetDTO.setSublinked_subgoals(new HashSet<>(
                subgoalShortMapper.mapToDto(new ArrayList<>(routine_from_db_opt.get().getSublinked_subgoals())))
        );
        return routineGetDTO;
    }

    @Transactional
    public ReflectionGetDTO getReflection(Long id) {
        Optional<Reflection> reflection_from_db_opt = reflectionRepository.findById(id);
        if (reflection_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!");
        }
        ReflectionGetDTO reflectionGetDTO = reflectionGetMapper.mapToDto(reflection_from_db_opt.get());
        if (reflection_from_db_opt.get().getGoal() != null) {
            reflectionGetDTO.setGoal_id(reflection_from_db_opt.get().getGoal().getId());
        } else if (reflection_from_db_opt.get().getGroupgoal() != null) {
            reflectionGetDTO.setGoal_id(reflection_from_db_opt.get().getGroupgoal().getId());
        }
        reflectionGetDTO.setResources(new HashSet<>(resourceShortMapper.mapToDto(new ArrayList<>(reflection_from_db_opt.get().getResources()))));
        reflectionGetDTO.setSublinked_entities(extractEntities(reflection_from_db_opt.get()));
        reflectionGetDTO.setSublinked_subgoals(new HashSet<>(
                subgoalShortMapper.mapToDto(new ArrayList<>(reflection_from_db_opt.get().getSublinked_subgoals())))
        );
        return reflectionGetDTO;
    }

    @Transactional
    public QuestionGetDTO getQuestion(Long id) {
        Optional<Question> question_from_db_opt = questionRepository.findById(id);
        if (question_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!");
        }
        QuestionGetDTO questionGetDTO = questionGetMapper.mapToDto(question_from_db_opt.get());
        if (question_from_db_opt.get().getGoal() != null) {
            questionGetDTO.setGoal_id(question_from_db_opt.get().getGoal().getId());
        } else if (question_from_db_opt.get().getGroupgoal() != null) {
            questionGetDTO.setGoal_id(question_from_db_opt.get().getGroupgoal().getId());
        }
        questionGetDTO.setResources(new HashSet<>(resourceShortMapper.mapToDto(new ArrayList<>(question_from_db_opt.get().getResources()))));
        questionGetDTO.setSublinked_entities(extractEntities(question_from_db_opt.get()));
        questionGetDTO.setSublinked_subgoals(new HashSet<>(
                subgoalShortMapper.mapToDto(new ArrayList<>(question_from_db_opt.get().getSublinked_subgoals())))
        );
        return questionGetDTO;
    }

    /****************************** DELETES ********************************/

    public MessageResponse deleteReflection(Long id) {
        Optional<Reflection> reflection_from_db_opt = reflectionRepository.findById(id);
        if (reflection_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!");
        }
        if(reflection_from_db_opt.get().getGoal() != null){
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
        if(question_from_db_opt.get().getGoal() != null){
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
        if(task_from_db_opt.get().getGoal() != null){
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
        if(routine_from_db_opt.get().getGoal() != null){
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
        routine_from_db_opt.get().setRating(null);
        routine_from_db_opt.get().setDeadline(null);
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
        deadlines.add(new Date(routine_from_db.getDeadline().get(routine_from_db.getDeadline().size()-1).getTime() +  routine_from_db.getPeriod() *(1000*60*60*24)));
        routine_from_db.setDeadline(deadlines);

        routineRepository.save(routine_from_db);

        return new MessageResponse("This deadline evaluated successfully, move on to next deadline!",MessageType.SUCCESS);
    }

    /********************************** TASK COMPLETE *****************/
    public MessageResponse completeTask(Long task_id, Long rating){
        Task task_from_db = taskRepository.findById(task_id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found!"));
        task_from_db.setRating(rating.doubleValue());
        task_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        task_from_db.setIsDone(Boolean.TRUE);
        taskRepository.save(task_from_db);
        return new MessageResponse("Task completed!",MessageType.SUCCESS);
    }
    /********************************** ROUTINE COMPLETE *****************/
    public MessageResponse completeRoutine(Long routine_id, Long rating){
        Routine routine_from_db = routineRepository.findById(routine_id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Routine not found!"));
        List<Double> ratings = routine_from_db.getRating();
        ratings.add(rating.doubleValue());
        routine_from_db.setRating(ratings);
        routine_from_db.setIsDone(Boolean.TRUE);
        routine_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        routineRepository.save(routine_from_db);

        return new MessageResponse("Routine completed!",MessageType.SUCCESS);
    }

    public MessageResponse completeQuestion(Long question_id) {
        Question question_from_db = questionRepository.findById(question_id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found!"));
        question_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        question_from_db.setIsDone(Boolean.TRUE);
        questionRepository.save(question_from_db);
        return new MessageResponse("Question completed!",MessageType.SUCCESS);

    }

    public MessageResponse completeRefection(Long reflection_id) {
        Reflection reflection_from_db = reflectionRepository.findById(reflection_id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reflection not found!"));
        reflection_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        reflection_from_db.setIsDone(Boolean.TRUE);
        reflectionRepository.save(reflection_from_db);
        return new MessageResponse("Reflection completed!",MessageType.SUCCESS);}
}
