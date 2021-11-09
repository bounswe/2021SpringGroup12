package cmpe451.group12.beabee.login.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {

    @NotNull
    private String username;

    private String password;

    @NotNull
    @Email(message = "E-mail must be valid!")
    private String email;

    private String name;
    private String surname;

}
