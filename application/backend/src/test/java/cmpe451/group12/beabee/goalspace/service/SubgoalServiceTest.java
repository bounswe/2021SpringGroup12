package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalPostMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalShortMapper;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubgoalServiceTest {

    private SubgoalGetMapper subgoalGetMapper;
    private SubgoalShortMapper subgoalShortMapper;
    private SubgoalPostMapper subgoalPostMapper;
    private SubgoalRepository subgoalRepository;
    private GoalRepository goalRepository;
    private GroupGoalRepository groupGoalRepository;
    private UserRepository userRepository;
    private EntitiShortMapper entitiShortMapper;

    private SubgoalService subgoalService;

    @Before
    public void setUp() {
        subgoalGetMapper = Mockito.mock(SubgoalGetMapper.class);
        subgoalShortMapper = Mockito.mock(SubgoalShortMapper.class) ;
        subgoalPostMapper = Mockito.mock(SubgoalPostMapper.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        goalRepository = Mockito.mock(GoalRepository.class);
        groupGoalRepository = Mockito.mock(GroupGoalRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        entitiShortMapper = Mockito.mock(EntitiShortMapper.class) ;

        subgoalService = new SubgoalService(subgoalGetMapper, subgoalShortMapper,subgoalPostMapper,subgoalRepository,goalRepository,groupGoalRepository,userRepository,entitiShortMapper);
    }

    @Test
    public void whenCompleteASubgoalWithUncompletedSubgoals_ItShouldReturnError(){
        Subgoal subgoal = new Subgoal();
        subgoal.setId(1L);
        subgoal.setIsDone(Boolean.FALSE);

        Subgoal child1 = new Subgoal();
        child1.setId(2L);
        child1.setIsDone(Boolean.TRUE);

        Subgoal child2 = new Subgoal();
        child2.setId(3L);
        child2.setIsDone(Boolean.FALSE);

        subgoal.setChild_subgoals(Stream.of(child1,child2).collect(Collectors.toSet()));

        Mockito.when(subgoalRepository.findById(1L)).thenReturn(java.util.Optional.of(subgoal));

        MessageResponse result = subgoalService.completeSubgoal(1L,4L);
        Assert.assertEquals(new MessageResponse("This subgoal has some subgoals that are uncompleted! Finish those first!", MessageType.ERROR), result);


    }

    @Test
    public void whenCompleteASubgoalWithAllCompletedSubgoals_ItShouldCalculateAverageRatingCorrect(){
        /*
        * Scenario: A subgoal is uncompleted. It has two subgoals with 1 and 3 ratings.
        *           This subgoal got 4 rating.
        *           Final rating should be (4 + ((1+3)/2) ) / 2 = 3.
        *           Since we calculate the rating with the average of (given value and average of its subgoals).
        * */
        Subgoal subgoal = new Subgoal();
        subgoal.setId(1L);
        subgoal.setIsDone(Boolean.FALSE);

        Subgoal child1 = new Subgoal();
        child1.setId(2L);
        child1.setRating(3D);
        child1.setIsDone(Boolean.TRUE);

        Subgoal child2 = new Subgoal();
        child2.setId(3L);
        child2.setRating(1D);
        child2.setIsDone(Boolean.TRUE);

        subgoal.setChild_subgoals(Stream.of(child1,child2).collect(Collectors.toSet()));

        Mockito.when(subgoalRepository.findById(1L)).thenReturn(java.util.Optional.of(subgoal));

        MessageResponse result = subgoalService.completeSubgoal(1L,4L);

        Assert.assertEquals(java.util.Optional.of(3D),java.util.Optional.of(subgoal.getRating()));

        Assert.assertEquals(new MessageResponse("Subgoal completed successfully!", MessageType.SUCCESS), result);

    }
}
