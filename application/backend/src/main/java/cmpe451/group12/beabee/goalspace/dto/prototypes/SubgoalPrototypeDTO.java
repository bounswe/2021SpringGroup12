package cmpe451.group12.beabee.goalspace.dto.prototypes;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SubgoalPrototypeDTO {

    private Long id;
    private Long reference_subgoal_id;
    private String title;
    private String description;
    private Set<SubgoalPrototypeDTO> child_subgoals;
}
