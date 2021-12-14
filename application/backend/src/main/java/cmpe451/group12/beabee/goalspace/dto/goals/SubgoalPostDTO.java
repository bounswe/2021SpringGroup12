package cmpe451.group12.beabee.goalspace.dto.goals;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class SubgoalPostDTO {
    private Long main_goal_id;
    private Long parent_subgoal_id;
    private Long main_groupgoal_id;

    private String title;
    private String description;
    private Date deadline;
}
