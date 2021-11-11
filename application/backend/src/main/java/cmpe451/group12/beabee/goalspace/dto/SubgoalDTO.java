package cmpe451.group12.beabee.goalspace.dto;


import cmpe451.group12.beabee.goalspace.enums.EntityType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class SubgoalDTO {

    private Long id;

    @NotBlank
    private Long mainGoal_id;

    //TODO, buna gerek var mı ya?
    private EntityType entityType;

    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;
    private Date deadline;
    private Double rating;
}
