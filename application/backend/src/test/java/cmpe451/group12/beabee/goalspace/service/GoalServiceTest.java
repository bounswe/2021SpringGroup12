package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalPostMapper;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GoalServiceTest {

    private GoalService goalService;
    private GoalRepository goalRepository;
    private GoalPostMapper goalPostMapper;
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
        goalRepository = Mockito.mock(GoalRepository.class);
        goalPostMapper = Mockito.mock(GoalPostMapper.class);
        userRepository = Mockito.mock(UserRepository.class);

        //goalService = new GoalService(goalRepository, goalPostMapper, userRepository);
    }

/*
    @Test
    public void whenGetGoalsOfAUserCalledWithValidRequest_ItShouldReturnListOfGoals() {
        //define 2 users
        Users user1 = new Users();
        user1.setUser_id(5L);

        Users user2 = new Users();
        user2.setUser_id(6L);

        //define 4 goals
        Goal goal1 = new Goal();
        goal1.setTitle("goal 1");
        goal1.setDescription("description 1");
        goal1.setId(1L);
        goal1.setCreator(user1);

        Goal goal2 = new Goal();
        goal2.setTitle("goal 2");
        goal2.setDescription("description 2");
        goal2.setId(2L);
        goal2.setCreator(user2);

        Goal goal3 = new Goal();
        goal3.setTitle("goal 3");
        goal3.setDescription("description 3");
        goal3.setId(3L);
        goal3.setCreator(user1);

        Goal goal4 = new Goal();
        goal4.setTitle("goal 4");
        goal4.setDescription("description 4");
        goal4.setId(4L);
        goal4.setCreator(user2);

        //define DTO's of the goals above
        GoalGetDTO goalGetDTO1 = new GoalGetDTO();
        goalGetDTO1.setTitle("goal 1");
        goalGetDTO1.setDescription("description 1");
        goalGetDTO1.setId(1L);

        GoalGetDTO goalGetDTO3 = new GoalGetDTO();
        goalGetDTO3.setTitle("goal 3");
        goalGetDTO3.setDescription("description 3");
        goalGetDTO3.setId(3L);

        //make list of the goals above
        List<Goal> goals_of_user1 = new ArrayList<>();
        goals_of_user1.add(goal1);
        goals_of_user1.add(goal3);

        //make list of the goalDTOs above
        List<GoalGetDTO> goaldtos_of_user1 = new ArrayList<>();
        goaldtos_of_user1.add(goalGetDTO1);
        goaldtos_of_user1.add(goalGetDTO3);

        // mock other classes
        Mockito.when(goalRepository.findAllByUserId(5L)).thenReturn(goals_of_user1);
        Mockito.when(goalPostMapper.mapToDto(goals_of_user1)).thenReturn(goaldtos_of_user1);

        //verify actual and expected results
        List<GoalGetDTO> result = goalService.getGoalsOfAUser(5L);

        Assert.assertEquals(goaldtos_of_user1, result);
        Mockito.verify(goalRepository).findAllByUserId(5L);
        Mockito.verify(goalPostMapper).mapToDto(goals_of_user1);
    }

    @Test
    public void whenCreateAGoalCalledWithValidRequest_ItShouldAppearInDatabase() {
        GoalGetDTO goalGetDTO = new GoalGetDTO();
        goalGetDTO.setTitle("goal 1");
        goalGetDTO.setDescription("description 1");

        Goal goal = new Goal();
        goal.setTitle("goal 1");
        goal.setDescription("description 1");
        goal.setId(1L);

        Users user = new Users();
        user.setUser_id(2L);

        Mockito.when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        Mockito.when(goalPostMapper.mapToEntity(goalGetDTO)).thenReturn(goal);
        Mockito.when(goalRepository.save(goal)).thenReturn(goal);

        MessageResponse result = goalService.createAGoal(2L, goalGetDTO);
        Assert.assertEquals(result, new MessageResponse("Goal added successfully.", MessageType.SUCCESS));
        Mockito.verify(userRepository).findById(2L);
        Mockito.verify(goalPostMapper).mapToEntity(goalGetDTO);
        Mockito.verify(goalRepository).save(goal);
    }

    @Test
    public void whenCreateAGoalCalledWithInvalidRequest_ItShouldNotAppearInDatabase() {

        GoalGetDTO goalGetDTO = new GoalGetDTO();
        goalGetDTO.setTitle("goal 1");
        goalGetDTO.setDescription("description 1");
        Goal goal = new Goal();
        goal.setTitle("goal 1");
        goal.setDescription("description 1");
        goal.setId(1L);

        Users user = null;

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(goalPostMapper.mapToEntity(goalGetDTO)).thenReturn(goal);
        Mockito.when(goalRepository.save(goal)).thenReturn(goal);

        MessageResponse result = goalService.createAGoal(2L, goalGetDTO);
        Assert.assertEquals(result, new MessageResponse("User does not exists!", MessageType.ERROR));
        Mockito.verify(userRepository).findById(2L);

    }

*/
}