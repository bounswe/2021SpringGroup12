package cmpe451.group12.beabee.goalspace.dto.prototypes;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class EntitiPrototypeDTO {
    private Long id;
    private Long referenced_entiti_id;
    private EntitiType entitiType;
    private Set<EntitiPrototypeDTO> child_entities;
    private String title;
    private String description;
    private Long period;
}
