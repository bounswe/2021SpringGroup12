package cmpe451.group12.beabee.goalspace.dto.entities;

import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReflectionPostDTO {

    private Long goal_id;
    private Long subgoal_id;

    private String title;
    private String description;

}
