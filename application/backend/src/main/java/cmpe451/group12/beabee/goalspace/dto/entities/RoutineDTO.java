package cmpe451.group12.beabee.goalspace.dto.entities;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoutineDTO {

    private Long id;

    private Long mainGoal_id;

    private EntitiType entitiType;
    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;
    private Long period;

}
