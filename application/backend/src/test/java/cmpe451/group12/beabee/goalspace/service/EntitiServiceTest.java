package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.*;
import cmpe451.group12.beabee.goalspace.dto.SubgoalDTO;
import cmpe451.group12.beabee.goalspace.dto.TaskDTO;
import cmpe451.group12.beabee.goalspace.mapper.*;
import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Subgoal;
import cmpe451.group12.beabee.goalspace.model.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class EntitiServiceTest {

    private EntitiService entitiService;
    private GoalRepository goalRepository;
    private EntitiMapper entitiMapper;
    private EntitiRepository entitiRepository;
    private SubgoalMapper subgoalMapper;
    private SubgoalRepository subgoalRepository;
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;
/*
    private ReflectionRepository reflectionRepository;
    private ReflectionMapper reflectionMapper;
    private RoutineRepository routineRepository;
    private RoutineMapper routineMapper;
    private QuestionRepository questionRepository;
    private QuestionMapper questionMapper;
*/

    @Before
    public void setUp() throws Exception {
        goalRepository = Mockito.mock(GoalRepository.class);
        entitiMapper = Mockito.mock(EntitiMapper.class);
        entitiRepository = Mockito.mock(EntitiRepository.class);
        subgoalMapper = Mockito.mock(SubgoalMapper.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        taskMapper = Mockito.mock(TaskMapper.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        entitiService = new EntitiService(goalRepository, entitiMapper, entitiRepository, subgoalMapper, subgoalRepository, taskRepository, taskMapper, null, null, null, null, null, null);
    }

    @Test
    public void whenLinkEntitiesCalledWithValidRequest_ItShouldReturnSuccess() {
        //define 2 entities
        Entiti entiti1 = new Subgoal();
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
    public void whenLinkEntitiesCalledWithInvalidRequest_ItShouldReturnError() {
        //define 2 entities
        Entiti entiti1 = new Subgoal();
        entiti1.setId(1L);
        entiti1.setSublinks(new HashSet<>());
        //mock other class calls
        Mockito.when(entitiRepository.findById(1L)).thenReturn(java.util.Optional.of(entiti1));
        Mockito.when(entitiRepository.save(entiti1)).thenReturn(entiti1);

        //assertions
        Assert.assertEquals(entitiService.linkEntities(1L, 2L), new MessageResponse("One of the entities does not exists!", MessageType.ERROR));
        Mockito.verify(entitiRepository).findById(1L);
    }

    @Test
    public void whenUpdateSubgoalCalledWithValidRequest_ItShouldReturnSuccess() {
        Subgoal old_subgoal = new Subgoal();
        old_subgoal.setId(1L);
        old_subgoal.setTitle("prev_title");

        Subgoal new_subgoal = new Subgoal();
        new_subgoal.setId(1L);
        new_subgoal.setTitle("new_title");

        SubgoalDTO subgoalDTO = new SubgoalDTO();
        subgoalDTO.setId(1L);
        subgoalDTO.setTitle("new_title");

        Mockito.when(subgoalRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(subgoalRepository.findById(1L)).thenReturn(java.util.Optional.of(new_subgoal));
        Mockito.when(subgoalMapper.mapToEntity(subgoalDTO)).thenReturn(new_subgoal);
        Mockito.when(subgoalRepository.save(new_subgoal)).thenReturn(new_subgoal);

        Assert.assertEquals(new MessageResponse("Updated subgoal", MessageType.SUCCESS), entitiService.updateSubgoal(subgoalDTO));
        Mockito.verify(subgoalRepository).existsById(1L);
        Mockito.verify(subgoalMapper).mapToEntity(subgoalDTO);
        Mockito.verify(subgoalRepository).save(new_subgoal);
        Assert.assertNotEquals(subgoalRepository.findById(1L).get().getTitle(), old_subgoal.getTitle());
    }

    @Test
    public void whenUpdateTaskCalledWithInvalidRequest_ItShouldReturnError() {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("new_title");

        Mockito.when(taskRepository.existsById(1L)).thenReturn(Boolean.FALSE);

        Assert.assertEquals(new MessageResponse("Couldn't update task!", MessageType.ERROR), entitiService.updateTask(taskDTO));
        Mockito.verify(taskRepository).existsById(1L);
    }

}