package cmpe451.group12.beabee.goalspace.dto.entities;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class EntitiDTO {

    private Long id;
    private Long goal_id;
    private Long groupgoal_id;
    private Long subgoal_id;
    private Date completedAt;
    private EntitiType entitiType;
    private String title;
    private String description;
    private Date createdAt;
    private Date completedAt;
    private Boolean isDone;
    private Date deadline;
    private Double rating;
    private Long period;

}
