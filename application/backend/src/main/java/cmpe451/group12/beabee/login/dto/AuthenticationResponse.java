package cmpe451.group12.beabee.login.dto;

import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.login.dto.UserDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationResponse {

    public final UserDTO userDTO;
    public final String jwt;
    public final String message;
    public final MessageType messageType;

}