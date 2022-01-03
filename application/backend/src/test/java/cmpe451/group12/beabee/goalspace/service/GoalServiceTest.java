package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.GoalPrototypeRespository;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class GoalServiceTest {

    private GoalService goalService;
    private GoalRepository goalRepository;
    private SubgoalRepository subgoalRepository;
    private GoalPostMapper goalPostMapper;
    private SubgoalPostMapper subgoalPostMapper;
    private SubgoalShortMapper subgoalShortMapper;
    private GoalGetMapper goalGetMapper;
    private GoalShortMapper goalShortMapper;
    private UserRepository userRepository;
    private EntitiShortMapper entitiShortMapper;
    private EntitiRepository entitiRepository;
    private RoutineRepository routineRepository;
    private ReflectionRepository reflectionRepository;
    private TaskRepository taskRepository;
    private QuestionRepository questionRepository;
    private TagRepository tagRepository;
    private GoalPrototypeRespository goalPrototypeRespository;
    private ActivityStreamService activityStreamService;

    @Before
    public void setUp() {
        goalRepository = Mockito.mock(GoalRepository.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        goalPostMapper = Mockito.mock(GoalPostMapper.class);
        subgoalPostMapper = Mockito.mock(SubgoalPostMapper.class);
        subgoalShortMapper = Mockito.mock(SubgoalShortMapper.class);
        goalGetMapper = Mockito.mock(GoalGetMapper.class);
        goalShortMapper = Mockito.mock(GoalShortMapper.class);
        userRepository = Mockito.mock(UserRepository.class);
        entitiShortMapper = Mockito.mock(EntitiShortMapper.class);
        entitiRepository = Mockito.mock(EntitiRepository.class);
        routineRepository = Mockito.mock(RoutineRepository.class);
        reflectionRepository = Mockito.mock(ReflectionRepository.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        questionRepository = Mockito.mock(QuestionRepository.class);
        tagRepository = Mockito.mock(TagRepository.class);
        goalPrototypeRespository = Mockito.mock(GoalPrototypeRespository.class);
        activityStreamService = Mockito.mock(ActivityStreamService.class);

        goalService = new GoalService(goalRepository, subgoalRepository, goalPostMapper,
                subgoalPostMapper, subgoalShortMapper, goalGetMapper, goalShortMapper,
                userRepository, entitiShortMapper, entitiRepository, routineRepository,
                reflectionRepository, taskRepository, questionRepository, tagRepository, goalPrototypeRespository, activityStreamService);
    }

    /* NO LONGER NEEDED SINCE WE REMOVED DEADLINES ***
     @Test public void whenExtendGoalCalledWithValidRequest_ItShouldReturnSuccess() {
     //parameters
     Long goal_id = 1L;
     Date five_days_later = new Date(System.currentTimeMillis() + 5 * 24 * 3600 * 1000);//5 days later than today
     Long prevExtensionCount = 5L;
     Date one_day_later = new Date(System.currentTimeMillis() + 1 * 24 * 3600 * 1000);

     Goal goal1 = new Goal();
     goal1.setTitle("goal 1");
     goal1.setDescription("description 1");
     goal1.setId(goal_id);
     goal1.setIsDone(Boolean.FALSE);
     goal1.setExtension_count(prevExtensionCount);

     Goal goal1_updated = new Goal();
     goal1_updated.setTitle("goal 1");
     goal1_updated.setDescription("description 1");
     goal1_updated.setId(goal_id);
     goal1_updated.setIsDone(Boolean.FALSE);
     goal1_updated.setExtension_count(prevExtensionCount + 1);

     // mock other classes
     Mockito.when(goalRepository.findById(goal_id)).thenReturn(java.util.Optional.of(goal1));
     Mockito.when(goalRepository.save(goal1)).thenReturn(goal1_updated);
     //verify actual and expected results
     MessageResponse result = goalService.extendGoal(goal_id, five_days_later);

     Assert.assertEquals(new MessageResponse("Goal extended successfully!", MessageType.SUCCESS), result);
     Mockito.verify(goalRepository).findById(goal_id);
     Mockito.verify(goalRepository).save(goal1);
     }

     @Test public void whenExtendGoalCalledWithInvalidRequest_ItShouldReturnError() {
     //parameters
     Long goal_id = 1L;
     Date five_days_later = new Date(System.currentTimeMillis() + 5 * 24 * 3600 * 1000);// 5 days later than today
     Long prevExtensionCount = 5L;
     Date one_day_later = new Date(System.currentTimeMillis() + 1 * 24 * 3600 * 1000);// 1 day later than today

     Goal goal1 = new Goal();
     goal1.setTitle("goal 1");
     goal1.setDescription("description 1");
     goal1.setId(goal_id);
     goal1.setIsDone(Boolean.FALSE);
     goal1.setExtension_count(prevExtensionCount);
     goal1.setDeadline(five_days_later);

     Goal goal1_updated = new Goal();
     goal1_updated.setTitle("goal 1");
     goal1_updated.setDescription("description 1");
     goal1_updated.setId(goal_id);
     goal1_updated.setIsDone(Boolean.FALSE);
     goal1_updated.setExtension_count(prevExtensionCount + 1);
     goal1_updated.setDeadline(one_day_later);

     // mock other classes
     Mockito.when(goalRepository.findById(goal_id)).thenReturn(java.util.Optional.of(goal1));
     //verify actual and expected results
     MessageResponse result = goalService.extendGoal(goal_id, five_days_later);

     Assert.assertEquals(new MessageResponse("New deadline must be later than current deadline!", MessageType.ERROR), result);
     Mockito.verify(goalRepository).findById(goal_id);
     }
     */
    @Test
    public void whenCompleteGoalCalledWithUncompletedGoals_ItShouldReturnError() {
        Long goal_id = 1L;

        Goal goal1 = new Goal();
        goal1.setId(goal_id);
        goal1.setIsDone(Boolean.FALSE);
        goal1.setRating(0D);

        Goal goal1_completed = new Goal();
        goal1_completed.setId(goal_id);
        goal1_completed.setIsDone(Boolean.TRUE);
        goal1_completed.setRating(4D);

        Subgoal subgoal1 = new Subgoal();
        subgoal1.setId(1L);
        subgoal1.setMainGoal(goal1);
        subgoal1.setRating(5D);
        subgoal1.setIsDone(Boolean.TRUE);

        Subgoal subgoal2 = new Subgoal();
        subgoal1.setId(2L);
        subgoal2.setMainGoal(goal1);
        subgoal2.setRating(3D);
        subgoal2.setIsDone(Boolean.FALSE);

        Subgoal subgoal2_completed = new Subgoal();
        subgoal1.setId(2L);
        subgoal2_completed.setMainGoal(goal1);
        subgoal2_completed.setRating(3D);
        subgoal2_completed.setIsDone(Boolean.TRUE);

        goal1.setSubgoals(Stream.of(subgoal1, subgoal2).collect(Collectors.toSet()));
        goal1_completed.setSubgoals(Stream.of(subgoal1, subgoal2_completed).collect(Collectors.toSet()));

        Mockito.when(goalRepository.findById(goal_id)).thenReturn(java.util.Optional.of(goal1));
        Mockito.when(subgoalRepository.saveAll(goal1.getSubgoals())).thenReturn(goal1_completed.getSubgoals().stream().collect(Collectors.toList()));
        Mockito.when(goalRepository.save(goal1)).thenReturn(goal1_completed);// key part: returns completed goal. So, updates were successful.

        MessageResponse result = goalService.completeGoal(goal_id);
        Assert.assertEquals(new MessageResponse("This goal has some subgoals that are uncompleted! Finish those first!", MessageType.ERROR), result);

        Mockito.verify(goalRepository).findById(goal_id);

    }

    @Test
    public void whenCompleteGoalCalledWithInvalidRequest_ItShouldReturnError() {
        Long goal_id = 1L;

        Goal goal1 = new Goal();
        goal1.setId(goal_id);
        goal1.setIsDone(Boolean.TRUE);
        goal1.setRating(0D);

        Mockito.when(goalRepository.findById(goal_id)).thenReturn(java.util.Optional.of(goal1));

        MessageResponse result = goalService.completeGoal(goal_id);
        Assert.assertEquals(new MessageResponse("Already completed!", MessageType.ERROR), result);

        Mockito.verify(goalRepository).findById(goal_id);

    }

    @Test
    public void whenGetAnalyticsCalledWithNoSubgoal_ItShouldReturnNullForManyFields() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setIsDone(Boolean.FALSE);
        goal.setSubgoals(new HashSet<>());

        GoalAnalyticsDTO goalAnalyticsDTO = new GoalAnalyticsDTO();
        goalAnalyticsDTO.setWorstSubgoal(null);
        goalAnalyticsDTO.setBestSubgoal(null);
        goalAnalyticsDTO.setLongestSubgoal(null);
        goalAnalyticsDTO.setShortestSubgoal(null);
        goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.ACTIVE);
        goalAnalyticsDTO.setGoal_id(1L);

        Mockito.when(goalRepository.findById(1L)).thenReturn(java.util.Optional.of(goal));

        GoalAnalyticsDTO result = goalService.getAnalytics(1L);

        Assert.assertEquals(goalAnalyticsDTO.getGoal_id(), result.getGoal_id());
        Assert.assertEquals(goalAnalyticsDTO.getStatus(), result.getStatus());
        Assert.assertEquals(goalAnalyticsDTO.getShortestSubgoal(), result.getShortestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getLongestSubgoal(), result.getLongestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getBestSubgoal(), result.getBestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getWorstSubgoal(), result.getWorstSubgoal());

        Mockito.verify(goalRepository).findById(1L);
    }

    @Test
    public void whenGetAnalyticsCalled_ItShouldClassifySubgoalsCorrectly() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setIsDone(Boolean.TRUE);
        goal.setCompletedAt(new Date(System.currentTimeMillis() - 15 * 24 * 3600 * 1000));
        goal.setCreatedAt(new Date(System.currentTimeMillis()));

        Subgoal shortest = new Subgoal();
        shortest.setId(1L);
        shortest.setCreatedAt(new Date(System.currentTimeMillis() - 2 * 24 * 3600 * 1000));
        shortest.setCompletedAt(new Date(System.currentTimeMillis() - 1 * 24 * 3600 * 1000));
        shortest.setIsDone(Boolean.TRUE);
        shortest.setMainGoal(goal);
        shortest.setRating(3D);

        Subgoal longest_and_worst = new Subgoal();
        longest_and_worst.setId(2L);
        longest_and_worst.setCreatedAt(new Date(System.currentTimeMillis() - 10 * 24 * 3600 * 1000));
        longest_and_worst.setCompletedAt(new Date(System.currentTimeMillis() - 1 * 24 * 3600 * 1000));
        longest_and_worst.setIsDone(Boolean.TRUE);
        longest_and_worst.setMainGoal(goal);
        longest_and_worst.setRating(1D);

        Subgoal best = new Subgoal();
        best.setId(3L);
        best.setCreatedAt(new Date(System.currentTimeMillis() - 5 * 24 * 3600 * 1000));
        best.setCompletedAt(new Date(System.currentTimeMillis() - 1 * 24 * 3600 * 1000));
        best.setIsDone(Boolean.TRUE);
        best.setMainGoal(goal);
        best.setRating(5D);

        goal.setSubgoals(Stream.of(shortest, longest_and_worst, best).collect(Collectors.toSet()));

        SubgoalDTOShort longest_and_worst_dto = new SubgoalDTOShort();
        longest_and_worst_dto.setId(2L);
        SubgoalDTOShort best_dto = new SubgoalDTOShort();
        best_dto.setId(3L);
        SubgoalDTOShort shortest_dto = new SubgoalDTOShort();
        shortest_dto.setId(1L);

        GoalAnalyticsDTO goalAnalyticsDTO = new GoalAnalyticsDTO();
        goalAnalyticsDTO.setWorstSubgoal(longest_and_worst_dto);
        goalAnalyticsDTO.setBestSubgoal(best_dto);
        goalAnalyticsDTO.setLongestSubgoal(longest_and_worst_dto);
        goalAnalyticsDTO.setShortestSubgoal(shortest_dto);
        goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.COMPLETED);
        goalAnalyticsDTO.setGoal_id(1L);


        Mockito.when(goalRepository.findById(1L)).thenReturn(java.util.Optional.of(goal));
        Mockito.when(subgoalShortMapper.mapToDto(goal.getSubgoals().stream().max(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get())).thenReturn(longest_and_worst_dto);
        Mockito.when(subgoalShortMapper.mapToDto(goal.getSubgoals().stream().min(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get())).thenReturn(shortest_dto);
        Mockito.when(subgoalShortMapper.mapToDto(goal.getSubgoals().stream().max(Comparator.comparing(Subgoal::getRating)).get())).thenReturn(best_dto);
        Mockito.when(subgoalShortMapper.mapToDto(goal.getSubgoals().stream().min(Comparator.comparing(Subgoal::getRating)).get())).thenReturn(longest_and_worst_dto);

        GoalAnalyticsDTO result = goalService.getAnalytics(1L);

        Assert.assertEquals(goalAnalyticsDTO.getGoal_id(), result.getGoal_id());
        Assert.assertEquals(goalAnalyticsDTO.getStatus(), result.getStatus());
        Assert.assertEquals(goalAnalyticsDTO.getShortestSubgoal(), result.getShortestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getLongestSubgoal(), result.getLongestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getBestSubgoal(), result.getBestSubgoal());
        Assert.assertEquals(goalAnalyticsDTO.getWorstSubgoal(), result.getWorstSubgoal());

        Mockito.verify(goalRepository).findById(1L);
    }

    @Test
    public void whenGetAGoalCalledWithInvalidId_ItShouldReturnNotFoundError() {
        Long goal_id = -1L;
        Mockito.when(goalRepository.findById(goal_id)).thenReturn(Optional.empty());
        Assert.assertThrows(ResponseStatusException.class, () -> {
            goalService.getAGoal(goal_id);
        });
    }

    @Test
    public void whenGetGoalsOfAUserCalledWithValidId_ItShouldReturnGoalsOfUser() {
        Users user1 = new Users();
        user1.setUser_id(1L);
        Users user2 = new Users();
        user2.setUser_id(2L);

        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setCreator(user1);

        Goal goal2 = new Goal();
        goal2.setId(2L);
        goal2.setCreator(user1);

        Goal goal3 = new Goal();
        goal3.setId(3L);
        goal3.setCreator(user2);

        GoalDTOShort goal1short = new GoalDTOShort();
        goal1short.setId(1L);

        GoalDTOShort goal2short = new GoalDTOShort();
        goal2short.setId(2L);

        Mockito.when(goalRepository.findAllByUserId(1L)).thenReturn(Stream.of(goal1, goal2).collect(Collectors.toList()));
        Mockito.when(goalShortMapper.mapToDto(Stream.of(goal1, goal2).collect(Collectors.toList()))).thenReturn(Stream.of(goal1short, goal2short).collect(Collectors.toList()));

        List<GoalDTOShort> result = goalService.getGoalsOfAUser(1L);

        Assert.assertEquals(Stream.of(goal1short, goal2short).collect(Collectors.toList()), result);

        Mockito.verify(goalRepository).findAllByUserId(1L);
        Mockito.verify(goalShortMapper).mapToDto(Stream.of(goal1, goal2).collect(Collectors.toList()));


    }

    @Test
    public void whenCopyGoalPrototypeCalledWithValidParameteres_ItShouldReturnSuccess() {

        GoalPrototype prototype = new GoalPrototype();
        prototype.setId(1L);
        prototype.setEntities(new HashSet<>());
        prototype.setTags(new HashSet<>());
        prototype.setHiddentags(new HashSet<>());
        prototype.setSubgoals(new HashSet<>());
        prototype.setReference_goal_id(1L);

        Goal goal = new Goal();
        goal.setDownloadCount(5L);

        Mockito.when(goalPrototypeRespository.findById(1L)).thenReturn(Optional.of(prototype));
        Mockito.when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));
        Mockito.when(goalRepository.save(goal)).thenReturn(goal);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));





    }

    @Test
    public void whenCopyGoalPrototypeCalledWithInvalidParameteres_ItShouldReturnError() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        Assert.assertThrows(ResponseStatusException.class, () -> {
            goalService.copyGoalPrototype(1L,1L);
        });
    }
}