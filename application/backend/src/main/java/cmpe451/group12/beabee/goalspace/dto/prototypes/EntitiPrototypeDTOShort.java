package cmpe451.group12.beabee.goalspace.dto.prototypes;

import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class EntitiPrototypeDTOShort {
    private Long id;
    private EntitiType entitiType;
    private String title;
    private String description;
}
