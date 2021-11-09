package cmpe451.group12.beabee.login.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    private String password;

    @NotBlank(message ="E-mail cannot be blank!")
    @Email(message = "E-mail must be valid!")
    private String email;
}
