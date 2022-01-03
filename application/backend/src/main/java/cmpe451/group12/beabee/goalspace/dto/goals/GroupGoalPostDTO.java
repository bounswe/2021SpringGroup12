package cmpe451.group12.beabee.goalspace.dto.goals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GroupGoalPostDTO
{
    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
