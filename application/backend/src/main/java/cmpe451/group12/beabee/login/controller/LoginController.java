package cmpe451.group12.beabee.login.controller;

import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.service.LoginService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
//@RequestMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RequestMapping(value = "/")
public class LoginController {
    private final LoginService service;

    @ApiOperation(value = "Logs in a user", notes = "One of the email or username can be used, not needed both.")
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"email\": \"string\",\n" +
                            "  \"password*\": \"string\",\n" +
                            "  \"username*\": \"string\"\n" +
                            "}"
            )
            ) ) UserCredentialsDTO userCredentialsDTO) throws Exception {
        return service.login(userCredentialsDTO);
    }

    @ApiOperation(value = "Signs up a user")
    @PostMapping("/signup")
    public MessageResponse signup(@RequestBody @ApiParam(
            value = "A JSON value representing a transaction. An example of the expected schema can be found down here.",
            examples = @Example(value =
            @ExampleProperty(
                    value = "{\n" +
                            "  \"email*\"*: \"string\",\n" +
                            "  \"password*\":* \"string\",\n" +
                            "  \"username*\": \"string\"\n" +
                            "}"
            )
            ) ) UserCredentialsDTO userCredentialsDTO) {
        return service.signup(userCredentialsDTO);
    }
}
