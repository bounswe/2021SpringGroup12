package cmpe451.group12.beabee.goalspace.dto.goals;


import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class SubgoalGetDTO {

    private Long id;


    private Long main_goal_id;

    private Long parent_subgoal_id;

    private String title;
    private String description;
    private Date createdAt;
    private Boolean isDone;
    private Date deadline;
    private Double rating;

    private Set<SubgoalDTOShort> sublinks;
    private Set<EntitiDTOShort> entities;

}
