package cmpe451.group12.beabee.goalspace.dto.analytics;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserAnalyticsDTO {

    private Long averageExtensionCount;
    private Double averageRating;
    private Long averageCompletionTimeOfGoalsInMiliseconds;

    private GoalDTOShort longestGoal;
    private GoalDTOShort shortestGoal;
    private GoalDTOShort bestGoal;
    private GoalDTOShort worstGoal;

    private Long completedGoalCount;
    private Long activeGoalCount;

}