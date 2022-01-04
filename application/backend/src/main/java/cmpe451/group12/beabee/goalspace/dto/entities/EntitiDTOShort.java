package cmpe451.group12.beabee.goalspace.dto.entities;


import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EntitiDTOShort {

    private Long id;
    private EntitiType entitiType;
    private String title;
    private String description;
    private Date deadline;
}
