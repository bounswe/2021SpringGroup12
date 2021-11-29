package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.goalspace.Repository.entities.EntitiRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.TaskRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiMapper;
import cmpe451.group12.beabee.goalspace.mapper.entities.TaskGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import org.junit.Before;
import org.mockito.Mockito;

public class EntitiServiceTest {

    private EntitiService entitiService;
    private GoalRepository goalRepository;
    private EntitiMapper entitiMapper;
    private EntitiRepository entitiRepository;
    private SubgoalGetMapper subgoalGetMapper;
    private SubgoalRepository subgoalRepository;
    private TaskRepository taskRepository;
    private TaskGetMapper taskGetMapper;
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
        subgoalGetMapper = Mockito.mock(SubgoalGetMapper.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        taskGetMapper = Mockito.mock(TaskGetMapper.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        //entitiService = new EntitiService(goalRepository,null, entitiMapper, entitiRepository, subgoalGetMapper, subgoalRepository, taskRepository, taskGetMapper, null, null, null, null, null, null);
    }
/*
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
    }*/
/*
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
*/
    /*
    @Test
    public void whenUpdateSubgoalCalledWithValidRequest_ItShouldReturnSuccess() {
        Subgoal old_subgoal = new Subgoal();
        old_subgoal.setId(1L);
        old_subgoal.setTitle("prev_title");

        Subgoal new_subgoal = new Subgoal();
        new_subgoal.setId(1L);
        new_subgoal.setTitle("new_title");

        SubgoalGetDTO subgoalGetDTO = new SubgoalGetDTO();
        subgoalGetDTO.setId(1L);
        subgoalGetDTO.setTitle("new_title");

        Mockito.when(subgoalRepository.existsById(1L)).thenReturn(Boolean.TRUE);
        Mockito.when(subgoalRepository.findById(1L)).thenReturn(java.util.Optional.of(new_subgoal));
        Mockito.when(subgoalGetMapper.mapToEntity(subgoalGetDTO)).thenReturn(new_subgoal);
        Mockito.when(subgoalRepository.save(new_subgoal)).thenReturn(new_subgoal);

        Assert.assertEquals(new MessageResponse("Updated subgoal", MessageType.SUCCESS), entitiService.updateSubgoal(subgoalGetDTO));
        Mockito.verify(subgoalRepository).existsById(1L);
        Mockito.verify(subgoalGetMapper).mapToEntity(subgoalGetDTO);
        Mockito.verify(subgoalRepository).save(new_subgoal);
        Assert.assertNotEquals(subgoalRepository.findById(1L).get().getTitle(), old_subgoal.getTitle());
    }

    @Test
    public void whenUpdateTaskCalledWithInvalidRequest_ItShouldReturnError() {

        TaskGetDTO taskGetDTO = new TaskGetDTO();
        taskGetDTO.setId(1L);
        taskGetDTO.setTitle("new_title");

        Mockito.when(taskRepository.existsById(1L)).thenReturn(Boolean.FALSE);

        Assert.assertEquals(new MessageResponse("Couldn't update task!", MessageType.ERROR), entitiService.updateTask(taskGetDTO));
        Mockito.verify(taskRepository).existsById(1L);
    }
*/
}