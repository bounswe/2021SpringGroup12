package cmpe451.group12.beabee.goalspace.dto;

import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.model.Entiti;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class GoalDTO {

    private Long id;

    private GoalType goalType;

    @NotBlank
    private String title;

    private Boolean isDone;

    @NotBlank
    private String description;

    private Date deadline;

    private Date createdAt;


}
