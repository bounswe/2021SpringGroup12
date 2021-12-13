package cmpe451.group12.beabee.goalspace.dto.goals;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class GoalDTOShort {
    private Long id;

    private String title;

    private String description;

    private Date deadline;
}