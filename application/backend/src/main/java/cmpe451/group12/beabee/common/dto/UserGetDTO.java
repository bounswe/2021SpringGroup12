package cmpe451.group12.beabee.common.dto;

import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserGetDTO {
    @NotNull(message = "User id cannot be blank!")
    private Long id;

    private UserCredentialsGetDTO userCredentials;
}
