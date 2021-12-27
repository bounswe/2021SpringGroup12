package cmpe451.group12.beabee.goalspace.dto.prototypes;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class GoalPrototypeDTO {
    private Long id;
    private Long reference_goal_id;
    private String title;
    private String description;
    private Set<EntitiPrototypeDTOShort> entities;
    private Set<SubgoalPrototypeDTOShort> subgoals;
    private Set<String> tags;
    private Long download_count;
}
