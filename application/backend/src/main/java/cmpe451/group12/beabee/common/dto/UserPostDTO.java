package cmpe451.group12.beabee.common.dto;

import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostDTO {

    private UserCredentialsGetDTO userCredentials;
}
