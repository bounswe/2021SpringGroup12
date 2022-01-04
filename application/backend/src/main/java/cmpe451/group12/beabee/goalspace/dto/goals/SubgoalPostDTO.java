package cmpe451.group12.beabee.goalspace.dto.goals;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubgoalPostDTO {
    private Long main_goal_id;
    private Long parent_subgoal_id;
    private Long main_groupgoal_id;

    private String title;
    private String description;
}
