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
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.resources.ResourceShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntitiServiceTest {

    GoalRepository goalRepository;
    GroupGoalRepository groupGoalRepository;
    UserRepository userRepository;

    EntitiMapper entitiMapper;
    EntitiShortMapper entitiShortMapper;
    EntitiRepository entitiRepository;

    SubgoalGetMapper subgoalGetMapper;
    SubgoalShortMapper subgoalShortMapper;
    SubgoalRepository subgoalRepository;
    TaskRepository taskRepository;
    TaskGetMapper taskGetMapper;
    ReflectionRepository reflectionRepository;
    ReflectionGetMapper reflectionGetMapper;
    ReflectionPostMapper reflectionPostMapper;
    TaskPostMapper taskPostMapper;
    QuestionPostMapper questionPostMapper;
    RoutinePostMapper routinePostMapper;
    RoutineRepository routineRepository;
    RoutineGetMapper routineGetMapper;
    QuestionRepository questionRepository;
    QuestionGetMapper questionGetMapper;

    ResourceRepository resourceRepository;
    ResourceShortMapper resourceShortMapper;

    EntitiService entitiService;
    @Before
    public void setUp() throws Exception {
        goalRepository = Mockito.mock(GoalRepository.class);
        groupGoalRepository = Mockito.mock(GroupGoalRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        entitiMapper = Mockito.mock(EntitiMapper.class);
        entitiShortMapper = Mockito.mock(EntitiShortMapper.class);
        entitiRepository = Mockito.mock(EntitiRepository.class);
        subgoalGetMapper = Mockito.mock(SubgoalGetMapper.class);
        subgoalShortMapper = Mockito.mock(SubgoalShortMapper.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        taskGetMapper = Mockito.mock(TaskGetMapper.class);
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
                subgoalShortMapper,
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
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for both of the entities
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create the parent entity
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_entities(new HashSet<>());
        task.setGoal(goal);

        //Create the child entity
        Task task2 = new Task();
        task2.setId(random.nextLong());
        task2.setGoal(goal);

        //Create the EntitiLinkDTO
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.ENTITI);
        entitiLinkDTO.setChildId(task2.getId());

        //Mock repository calls to return the entities we created
        Mockito.when(entitiRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(entitiRepository.findById(task2.getId())).thenReturn(Optional.of(task2));

        //Get the result of the method call and check it is as expected.
        MessageResponse actual = entitiService.entitiLink(task.getId(), entitiLinkDTO);
        Assert.assertEquals(new MessageResponse("Linking operation is successful.", MessageType.SUCCESS), actual);

        //Add child entity to parent entiti's sublinks list
        task.getSublinked_entities().add(task2);

        //Check if there is as call to entiti repo to save the new parent with child in the sublinks list
        Mockito.verify(entitiRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void whenLinkEntitiesCalledWithInvalidRequest_ItShouldReturnNotFoundError() {
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for the parent
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create a parent goal for the child
        Goal goal2 = new Goal();
        goal2.setId(random.nextLong());

        //Create parent entiti
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_entities(new HashSet<>());
        task.setGoal(goal);

        //Create child entiti
        Task task2 = new Task();
        task2.setId(random.nextLong());
        task2.setGoal(goal2);

        //Create entiti link
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.ENTITI);
        entitiLinkDTO.setChildId(task2.getId());

        // Mock the repository calls.
        Mockito.when(entitiRepository.findById(task.getId()))
                .thenReturn(Optional.empty()) //First return null to emulate parent not found
                .thenReturn(Optional.of(task)); // Then return the true entiti

        Mockito.when(entitiRepository.findById(entitiLinkDTO.getChildId()))
                .thenReturn(Optional.empty())// First return null to emulate the child not found
                .thenReturn(Optional.of(task2)); // Then return true entiti to emulate parent and child is not in the same goal

        // Make three calls to get three fail situations
        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception3 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        //Check if the exceptions are caused by the reason we expected
        Assert.assertEquals("Parent entity not found", exception1.getReason());
        Assert.assertEquals("Child entity not found", exception2.getReason());
        Assert.assertEquals("Child entity is not in the same group!", exception3.getReason());

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

    @Test
    public void linkEntitiesWithSubgoalsTest_success() {
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for both entiti and subgoal
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create entiti
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_subgoals(new HashSet<>());
        task.setGoal(goal);

        //Create subgoal
        Subgoal subgoal = new Subgoal();
        subgoal.setId(random.nextLong());
        subgoal.setMainGoal(goal);

        //Create entiti link
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.SUBGOAL);
        entitiLinkDTO.setChildId(subgoal.getId());

        // Mock the calls to the repositories
        Mockito.when(entitiRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(subgoalRepository.findById(subgoal.getId())).thenReturn(Optional.of(subgoal));


        //Get the actual message response from method call and check if it is as expected
        MessageResponse actual = entitiService.entitiLink(task.getId(), entitiLinkDTO);
        Assert.assertEquals(new MessageResponse("Linking operation is successful.", MessageType.SUCCESS), actual);

        // Add the subgoal to the sublinks of parent entiti
        task.getSublinked_subgoals().add(subgoal);

        // Check if the save call to repository includes the subgoal
        Mockito.verify(entitiRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void linkEntitiesWithSubgoalsTest_fail() {
        //Random class to generate Ids.
        Random random = new Random();

        //Creat goal for entiti
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create another goal for subgoal
        Goal goal2 = new Goal();
        goal2.setId(random.nextLong());

        // Create the entiti
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_subgoals(new HashSet<>());
        task.setGoal(goal);

        // Create the subgoal
        Subgoal subgoal = new Subgoal();
        subgoal.setId(random.nextLong());
        subgoal.setMainGoal(goal2);

        //Create the entiti link
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.SUBGOAL);
        entitiLinkDTO.setChildId(subgoal.getId());

        // Mock the repo calls
        Mockito.when(entitiRepository.findById(task.getId()))
                .thenReturn(Optional.empty())//First return empty to emulate parent not found
                .thenReturn(Optional.of(task)); // Then return the correct entiti for second and third run.

        Mockito.when(subgoalRepository.findById(entitiLinkDTO.getChildId()))
                .thenReturn(Optional.empty())//Return empty in the first call(second run) to emulate the child not found.
                .thenReturn(Optional.of(subgoal));// Then return the correct subgoal(third run) to emulate the not in same goal.

        // Get exceptions from the method calls
        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception3 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiLink(task.getId(), entitiLinkDTO);
        });

        //Check the exceptions to make sure we get the right exception for the right error situtaion.
        Assert.assertEquals("Parent entity not found", exception1.getReason());
        Assert.assertEquals("Child subgoal not found", exception2.getReason());
        Assert.assertEquals("Child subgoal is not in the same group!", exception3.getReason());
    }

    @Test
    public void entitiDeleteLinkEntitiTest_fail() {
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for the parent and the child
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create parent entiti with no child
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_entities(new HashSet<>());
        task.setGoal(goal);

        //Create child entiti
        Task task2 = new Task();
        task2.setId(random.nextLong());
        task2.setGoal(goal);

        //Create entiti link
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.ENTITI);
        entitiLinkDTO.setChildId(task2.getId());

        // Mock the repository calls.
        Mockito.when(entitiRepository.findById(task.getId()))
                .thenReturn(Optional.empty()) //First return null to emulate parent not found
                .thenReturn(Optional.of(task)); // Then return the true entiti

        Mockito.when(entitiRepository.findById(entitiLinkDTO.getChildId()))
                .thenReturn(Optional.empty())// First return null to emulate the child not found
                .thenReturn(Optional.of(task2)); // Then return true entiti to emulate the link does not exist

        // Make three calls to get three fail situations
        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO);
        });


        //Check if the exceptions are caused by the reason we expected
        Assert.assertEquals("Parent entity not found", exception1.getReason());
        Assert.assertEquals("Child entity not found", exception2.getReason());

        //Case for non-existing link
        Assert.assertEquals(new MessageResponse("Link does not exist", MessageType.ERROR), entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO));
    }

    @Test
    public void entitiDeleteLinkEntitiTest_success() {
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for both of the entities
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create the parent entity
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_entities(new HashSet<>());
        task.setGoal(goal);

        //Create the child entity
        Task task2 = new Task();
        task2.setId(random.nextLong());
        task2.setGoal(goal);

        // Add child to sublinks list
        task.getSublinked_entities().add(task2);

        //Create the EntitiLinkDTO
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.ENTITI);
        entitiLinkDTO.setChildId(task2.getId());

        //Mock repository calls to return the entities we created
        Mockito.when(entitiRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Mockito.when(entitiRepository.findById(task2.getId())).thenReturn(Optional.of(task2));

        //Get the result of the method call and check it is as expected.
        MessageResponse actual = entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO);
        Assert.assertEquals(new MessageResponse("Link deleted is successful.", MessageType.SUCCESS), actual);

        //Remove child entity to parent entiti's sublinks list
        task.getSublinked_entities().remove(task2);

        //Check if there is as call to entiti repo to save the new parent with deleted child in the sublinks list
        Mockito.verify(entitiRepository, Mockito.times(1)).save(task);

    }

    @Test
    public void entitiDeleteLinkSubgoalTest_fail() {
        //Random class to generate Ids.
        Random random = new Random();

        //Create a parent goal for the parent and the child
        Goal goal = new Goal();
        goal.setId(random.nextLong());

        //Create parent entiti with no child
        Task task = new Task();
        task.setId(random.nextLong());
        task.setSublinked_subgoals(new HashSet<>());
        task.setGoal(goal);

        //Create child subgoal
        Subgoal subgoal = new Subgoal();
        subgoal.setId(random.nextLong());
        subgoal.setMainGoal(goal);

        //Create entiti link
        EntitiLinkDTO entitiLinkDTO = new EntitiLinkDTO();
        entitiLinkDTO.setChildType(LinkType.SUBGOAL);
        entitiLinkDTO.setChildId(subgoal.getId());

        // Mock the repository calls.
        Mockito.when(entitiRepository.findById(task.getId()))
                .thenReturn(Optional.empty()) //First return null to emulate parent not found
                .thenReturn(Optional.of(task)); // Then return the true entiti

        Mockito.when(subgoalRepository.findById(entitiLinkDTO.getChildId()))
                .thenReturn(Optional.empty())// First return null to emulate the child not found
                .thenReturn(Optional.of(subgoal)); // Then return true subgoal to emulate the link does not exist

        // Make three calls to get three fail situations
        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO);
        });


        //Check if the exceptions are caused by the reason we expected
        Assert.assertEquals("Parent entity not found", exception1.getReason());
        Assert.assertEquals("Child subgoal not found", exception2.getReason());

        //Case for non-existing link
        Assert.assertEquals(new MessageResponse("Link does not exist", MessageType.ERROR), entitiService.entitiDeleteLink(task.getId(), entitiLinkDTO));

    }
}