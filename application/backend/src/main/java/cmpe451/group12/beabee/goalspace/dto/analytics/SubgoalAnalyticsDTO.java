package cmpe451.group12.beabee.goalspace.dto.analytics;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SubgoalAnalyticsDTO {
    public enum Status {
        ACTIVE,
        COMPLETED;
    }

    private Long subgoal_id;
    private Status status;
    private Long extensionCount;
    private Double rating;

    private Date startTime;
    private Date finishTime;
    private Long completionTimeInMiliseconds; // client side should convert this to day and hours and minutes

    private SubgoalDTOShort longestSubgoal;
    private SubgoalDTOShort bestSubgoal;
    private SubgoalDTOShort worstSubgoal;
    private SubgoalDTOShort shortestSubgoal;

    private Long averageCompletionTimeOfSubgoals;
    private Long activeSubgoalCount ;
    private Long completedSubgoalCount ;
}
