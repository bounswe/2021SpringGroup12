package cmpe451.group12.beabee.goalspace.dto;

import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {
    @NotNull(message = "User id cannot be blank!")
    private Long id;

    private UserCredentialsDTO userCredentials;
}
