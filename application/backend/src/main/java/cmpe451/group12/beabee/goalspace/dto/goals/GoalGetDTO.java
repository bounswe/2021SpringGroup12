package cmpe451.group12.beabee.goalspace.dto.goals;

import cmpe451.group12.beabee.goalspace.enums.GoalType;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class GoalGetDTO {

    private Long id;

    private Long user_id;

    private GoalType goalType;

    private String title;

    private Boolean isDone;

    private String description;

    private Date deadline;

    private Date createdAt;

}
