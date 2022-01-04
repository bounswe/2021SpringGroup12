package cmpe451.group12.beabee.goalspace.dto.goals;

import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class GoalGetDTO {

    private Long id;

    private Long user_id;

    private GoalType goalType;
    private Date completedAt;
    private String title;

    private Boolean isDone;

    private String description;
    private Boolean isPublished;


    private Date createdAt;

    private Set<SubgoalDTOShort> subgoals;

    private Set<EntitiDTOShort> linkedEntities;
    private Set<String> tags;

}
