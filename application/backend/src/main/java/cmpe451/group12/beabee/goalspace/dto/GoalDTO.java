package cmpe451.group12.beabee.goalspace.dto;

import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.model.Entiti;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class GoalDTO {

    private Long id;

    private GoalType goalType;

    private String title;

    private Boolean isDone;

    private String description;

    private Date deadline;

    private Date createdAt;


}
