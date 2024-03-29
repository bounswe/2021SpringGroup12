package cmpe451.group12.beabee.login.dto;


import lombok.*;

import javax.persistence.Access;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsGetDTO {

    private Long user_id;

    @NotBlank(message = "Username cannot be blank!")
    private String username;

    private String password;

    @NotBlank(message ="E-mail cannot be blank!")
    @Email(message = "E-mail must be valid!")
    private String email;
}
