package cmpe451.group12.beabee.goalspace.dto.entities;

import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoutinePostDTO {
    private ParentGoalType goalType;
    private Long goalId;
    private LinkType initialLinkType;
    private Long initialParentId;
    private String title;
    private String description;
    private Date deadline;
    private Long period;
}
