package cmpe451.group12.beabee.common.dto;

import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.dto.UserDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationResponse {

    public final UserDTO userDTO;
    public final String jwt;
    public final String message;
    public final MessageType messageType;

}