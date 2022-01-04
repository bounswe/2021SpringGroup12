package cmpe451.group12.beabee.common.dto;

import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDTO {
    @NotNull(message = "User id cannot be blank!")
    private Long id;
    private Long followerCount;//todo
    private Long followingCount;//todo
    private UserCredentialsGetDTO userCredentials;
}
