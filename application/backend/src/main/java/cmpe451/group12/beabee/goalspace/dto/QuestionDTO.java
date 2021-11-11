package cmpe451.group12.beabee.goalspace.dto;


import cmpe451.group12.beabee.goalspace.enums.EntityType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuestionDTO {

    private Long id;

    private Long mainGoal_id;

    private EntityType entityType;
    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;

}
