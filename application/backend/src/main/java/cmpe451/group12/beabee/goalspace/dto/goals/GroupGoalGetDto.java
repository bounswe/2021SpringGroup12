package cmpe451.group12.beabee.goalspace.dto.goals;

import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class GroupGoalGetDto
{

    private Long id;

    private Long user_id;

    private GoalType goalType;

    private String title;

    private Boolean isDone;

    private String description;

    private Date createdAt;

    private Set<SubgoalDTOShort> subgoals;

    private Set<EntitiDTOShort> entities;

    private String token;

    private Set<UserCredentialsGetDTO> members;
}
