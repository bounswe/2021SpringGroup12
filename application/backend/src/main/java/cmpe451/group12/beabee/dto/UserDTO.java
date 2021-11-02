package cmpe451.group12.beabee.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {

    @JsonProperty("username")
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    @NotBlank(message ="E-mail cannot be blank!")
    @Email(message = "E-mail must be valid!")
    private String email;

    @JsonCreator
    public UserDTO(
                   @JsonProperty("username") String username,
                   @JsonProperty("password") String password,
                   @JsonProperty("email") String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
