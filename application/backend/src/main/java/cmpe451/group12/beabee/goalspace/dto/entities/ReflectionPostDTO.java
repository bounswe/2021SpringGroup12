package cmpe451.group12.beabee.goalspace.dto.entities;

import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReflectionPostDTO {

    private ParentType parentType;
    private Long parent_id;
    private String title;
    private String description;

}