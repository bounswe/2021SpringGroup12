package cmpe451.group12.beabee.goalspace.dto.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReflectionPostDTO {

    private ParentGoalType goalType;
    private Long goalId;
    private LinkType initialLinkType;
    private Long initialParentId;
    private String title;
    private String description;

}
