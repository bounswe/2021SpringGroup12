package cmpe451.group12.beabee.goalspace.dto.goals;

import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    private Set<String> tags;
}
