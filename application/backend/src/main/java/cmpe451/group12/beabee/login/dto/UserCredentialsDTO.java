package cmpe451.group12.beabee.login.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserCredentialsDTO {

    private Long user_id;

    @NotBlank(message = "Username cannot be blank!")
    private String username;

    private String password;

    @NotBlank(message ="E-mail cannot be blank!")
    @Email(message = "E-mail must be valid!")
    private String email;
}
