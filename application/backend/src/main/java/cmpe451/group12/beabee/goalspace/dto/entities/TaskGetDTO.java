package cmpe451.group12.beabee.goalspace.dto.entities;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class TaskGetDTO {

    private Long id;

    private Long goal_id;
    private Long subgoal_id;
    private EntitiType entitiType;
    private String title;
    private String description;
    private Date createdAt;
    private Date deadline;
    private Boolean isDone;
    private Set<EntitiShortDTO> sublinks;

}
