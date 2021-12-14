package cmpe451.group12.beabee.goalspace.dto.entities;


import cmpe451.group12.beabee.goalspace.dto.resources.ResourceDTOShort;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class QuestionGetDTO {

    private Long id;
    private Long goal_id;
    private Long groupgoal_id;
    private Long subgoal_id;
    private EntitiType entitiType;
    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;

    private Set<EntitiDTOShort> sublinks;
    private Set<ResourceDTOShort> resources;

}
