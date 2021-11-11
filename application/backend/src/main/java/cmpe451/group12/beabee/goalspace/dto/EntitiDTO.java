package cmpe451.group12.beabee.goalspace.dto;


import cmpe451.group12.beabee.goalspace.enums.EntityType;
import cmpe451.group12.beabee.goalspace.model.Goal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
public class EntitiDTO {

    private Long id;
    private Long mainGoal_id;
    private EntityType entityType;
    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;
    private Date deadline;
    private Double rating;
    private Long period;

}
