package cmpe451.group12.beabee.goalspace.dto.resources;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ResourceDTOShort {

    private Long id;
    private String name;
    private String contentType;
    private Date createdAt;

}
