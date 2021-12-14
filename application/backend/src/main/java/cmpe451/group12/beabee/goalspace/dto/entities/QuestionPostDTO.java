package cmpe451.group12.beabee.goalspace.dto.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuestionPostDTO {

    private ParentType parentType;
    private Long parent_id;
    private String title;
    private String description;
    private Date deadline;

}
