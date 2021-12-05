package cmpe451.group12.beabee.goalspace.dto.goals;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class GroupGoalPostDTO
{
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Date deadline;
}
