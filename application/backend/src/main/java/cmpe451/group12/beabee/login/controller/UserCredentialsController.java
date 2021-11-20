package cmpe451.group12.beabee.login.controller;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.service.UserCredentialsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/users")
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    @ApiOperation(value = "Get the info of a user.")
    @GetMapping("/{username}")
    public UserCredentialsDTO getUser(@PathVariable @ApiParam(value = "Username of the user.",example = "string")String username) {
        return userCredentialsService.getUserByUsername(username);
    }

    @ApiOperation(value = "Sends a password reset token to user's email address.", notes = "One of the email or username can be used, not needed both.")
    @GetMapping("/forgot/{email_or_username}")
    public MessageResponse forgotPassword(@PathVariable @ApiParam(value = "Email or username of the user.",example = "string")String email_or_username) throws MessagingException {
        return userCredentialsService.sendResetPasswordMail(email_or_username);
    }

    @ApiOperation(value = "Resets a user's password")
    @PostMapping("/reset/{token}")
    public MessageResponse ResetPassword(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"email*\": \"string\",\n" +
                            "  \"password*\": \"string\",\n" +
                            "  \"username*\": \"string\"\n" +
                            "}"
            )
            ) ) UserCredentialsDTO user, @PathVariable @ApiParam(value = "Password reset token.",example = "string")String token) {
        return userCredentialsService.resetPassword(user,token);
    }


}
