package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiLinkDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.LinkType;
import cmpe451.group12.beabee.goalspace.dto.entities.RoutineGetDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.TaskGetDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.*;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitiServiceTest {

    private GoalRepository goalRepository;
    private GroupGoalRepository groupGoalRepository;
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
        groupGoalRepository = Mockito.mock(GroupGoalRepository.class);
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
                groupGoalRepository,
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
        entiti1.setSublinked_entities(new HashSet<>());
        Entiti entiti2 = new Task();
        entiti2.setId(2L);

        //mock other class calls
        Mockito.when(entitiRepository.findById(1L)).thenReturn(java.util.Optional.of(entiti1));
        Mockito.when(entitiRepository.findById(2L)).thenReturn(java.util.Optional.of(entiti2));
        Mockito.when(entitiRepository.save(entiti1)).thenReturn(entiti1);

        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildId(2L);
        entitiLinkDTO.setChildType(LinkType.ENTITI);

        //assertions
        Assert.assertEquals(entitiService.entitiLink(1L, entitiLinkDTO), new MessageResponse("Linking operation is successful.", MessageType.SUCCESS));
        Mockito.verify(entitiRepository).findById(1L);
        Mockito.verify(entitiRepository).findById(2L);
        Mockito.verify(entitiRepository).save(entiti1);
    }

    @Test
    public void whenLinkEntitiesCalledWithInvalidRequest_ItShouldReturnNotFoundError() {
        //define 2 entities
        Entiti entiti1 = new Reflection();
        entiti1.setId(1L);
        entiti1.setSublinked_entities(new HashSet<>());

        //mock other class calls
        Mockito.when(entitiRepository.findById(1L)).thenReturn(java.util.Optional.of(entiti1));

        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildId(2L);
        entitiLinkDTO.setChildType(LinkType.ENTITI);

        //assertions
        Assert.assertThrows(ResponseStatusException.class, ()-> {entitiService.entitiLink(1L, entitiLinkDTO);});
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

    @Test
    public void whenRateRoutineCalledWithValidParameters_ItShouldReturnSuccess(){
        Long routine_id_param = 1L;
        Long rating_param = 4L;

        Routine routine = new Routine();
        routine.setId(routine_id_param);
        routine.setPeriod(3L);
        routine.setRating(Stream.of(1D,3D).collect(Collectors.toList()));
        Date first_deadline = new Date(System.currentTimeMillis());
        Date second_deadline = new Date(first_deadline.getTime() + routine.getPeriod() * (1000 * 60 * 60 * 24));
        Date third_deadline = new Date(first_deadline.getTime() + routine.getPeriod() * 2 * (1000 * 60 * 60 * 24));
        routine.setDeadline(Stream.of(first_deadline,second_deadline,third_deadline).collect(Collectors.toList()));

        Mockito.when(routineRepository.findById(routine_id_param)).thenReturn(Optional.of(routine));
        Mockito.when(routineRepository.save(routine)).thenReturn(routine);

        MessageResponse result = entitiService.rateRoutine(routine_id_param,rating_param);

        Assert.assertEquals(new MessageResponse("This deadline evaluated successfully, move on to next deadline!",MessageType.SUCCESS), result);
        Assert.assertEquals(routine.getDeadline().get(3),new Date(first_deadline.getTime() + routine.getPeriod() * 3 * (1000 * 60 * 60 * 24)));
        Assert.assertEquals(routine.getRating(),Stream.of(1D,3D,4D).collect(Collectors.toList()));

        Mockito.verify(routineRepository).findById(routine_id_param);
        Mockito.verify(routineRepository).save(routine);
    }

}