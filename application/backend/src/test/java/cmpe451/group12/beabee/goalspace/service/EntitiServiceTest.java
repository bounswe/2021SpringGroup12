package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.RoutineGetDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.TaskGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.*;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;

public class EntitiServiceTest {

    private GoalRepository goalRepository;
    private UserRepository userRepository;
    private EntitiMapper entitiMapper;
    private EntitiShortMapper entitiShortMapper;
    private EntitiRepository entitiRepository;
    private SubgoalGetMapper subgoalGetMapper;
    private SubgoalRepository subgoalRepository;
    private TaskRepository taskRepository;
    private TaskGetMapper taskGetMapper;
    private ReflectionRepository reflectionRepository;
    private ReflectionGetMapper reflectionGetMapper;
    private ReflectionPostMapper reflectionPostMapper;
    private TaskPostMapper taskPostMapper;
    private QuestionPostMapper questionPostMapper;
    private RoutinePostMapper routinePostMapper;
    private RoutineRepository routineRepository;
    private RoutineGetMapper routineGetMapper;
    private QuestionRepository questionRepository;
    private QuestionGetMapper questionGetMapper;
    private ResourceRepository resourceRepository;
    private ResourceShortMapper resourceShortMapper;
    private EntitiService entitiService;

    @Before
    public void setUp() throws Exception {
        goalRepository = Mockito.mock(GoalRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        entitiMapper = Mockito.mock(EntitiMapper.class);
        entitiShortMapper = Mockito.mock(EntitiShortMapper.class);
        entitiRepository = Mockito.mock(EntitiRepository.class);
        subgoalGetMapper = Mockito.mock(SubgoalGetMapper.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        taskGetMapper = Mockito.mock(TaskGetMapper.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        reflectionRepository = Mockito.mock(ReflectionRepository.class);
        reflectionGetMapper = Mockito.mock(ReflectionGetMapper.class);
        reflectionPostMapper = Mockito.mock(ReflectionPostMapper.class);
        taskPostMapper = Mockito.mock(TaskPostMapper.class);
        questionPostMapper = Mockito.mock(QuestionPostMapper.class);
        routinePostMapper = Mockito.mock(RoutinePostMapper.class);
        routineRepository = Mockito.mock(RoutineRepository.class);
        routineGetMapper = Mockito.mock(RoutineGetMapper.class);
        questionRepository = Mockito.mock(QuestionRepository.class);
        questionGetMapper = Mockito.mock(QuestionGetMapper.class);
        resourceRepository = Mockito.mock(ResourceRepository.class);
        resourceShortMapper = Mockito.mock(ResourceShortMapper.class);


        entitiService = new EntitiService(goalRepository,
                userRepository,
                entitiMapper,
                entitiShortMapper,
                entitiRepository,
                subgoalGetMapper,
                subgoalRepository,
                taskRepository,
                taskGetMapper,
                reflectionRepository,
                reflectionGetMapper,
                reflectionPostMapper,
                taskPostMapper,
                questionPostMapper,
                routinePostMapper,
                routineRepository,
                routineGetMapper,
                questionRepository,
                questionGetMapper,
                resourceRepository,
                resourceShortMapper);
    }

    @Test
    public void whenLinkEntitiesCalledWithValidRequest_ItShouldReturnSuccess() {
        //define 2 entities
        Entiti entiti1 = new Task();
        entiti1.setId(1L);
        entiti1.setSublinks(new HashSet<>());
        Entiti entiti2 = new Task();
        entiti2.setId(2L);

        //mock other class calls
        Mockito.when(entitiRepository.findById(1L)).thenReturn(java.util.Optional.of(entiti1));
        Mockito.when(entitiRepository.findById(2L)).thenReturn(java.util.Optional.of(entiti2));
        Mockito.when(entitiRepository.save(entiti1)).thenReturn(entiti1);

        //assertions
        Assert.assertEquals(entitiService.linkEntities(1L, 2L), new MessageResponse("Linking operation is successful.", MessageType.SUCCESS));
        Mockito.verify(entitiRepository).findById(1L);
        Mockito.verify(entitiRepository).findById(2L);
        Mockito.verify(entitiRepository).save(entiti1);
    }

    @Test
    public void whenLinkEntitiesCalledWithInvalidRequest_ItShouldReturnNotFoundError() {
        //define 2 entities
        Entiti entiti1 = new Reflection();
        entiti1.setId(1L);
        entiti1.setSublinks(new HashSet<>());

        //mock other class calls
        Mockito.when(entitiRepository.findById(1L)).thenReturn(java.util.Optional.of(entiti1));

        //assertions
        Assert.assertThrows(ResponseStatusException.class, ()-> {entitiService.linkEntities(1L, 2L);});
        Mockito.verify(entitiRepository).findById(1L);
    }

    @Test
    public void whenUpdateRoutineCalledWithValidRequest_ItShouldReturnSuccess() {
        RoutineGetDTO routineGetDTO = new RoutineGetDTO();
        routineGetDTO.setId(1L);
        routineGetDTO.setTitle("new_title");

        Routine old_routine = new Routine();
        old_routine.setId(routineGetDTO.getId());
        old_routine.setTitle("prev_title");

        Routine new_routine = new Routine();
        new_routine.setId(routineGetDTO.getId());
        new_routine.setTitle(routineGetDTO.getTitle());



        Mockito.when(routineRepository.findById(routineGetDTO.getId())).thenReturn(Optional.of(old_routine));
        Mockito.when(routineRepository.save(new_routine)).thenReturn(new_routine);
        Assert.assertEquals(new MessageResponse("Updated routine", MessageType.SUCCESS), entitiService.updateRoutine(routineGetDTO));
        Mockito.verify(routineRepository).findById(1L);
        Mockito.verify(routineRepository).save(new_routine);
    }

    @Test
    public void whenUpdateTaskCalledWithInvalidId_ItShouldReturnNotFoundError() {
        TaskGetDTO taskGetDTO = new TaskGetDTO();
        taskGetDTO.setId(1L);
        taskGetDTO.setTitle("new_title");

        Mockito.when(taskRepository.findById(taskGetDTO.getId())).thenReturn(Optional.empty());
        Assert.assertThrows(ResponseStatusException.class, ()-> {entitiService.updateTask(taskGetDTO);});
    }



}