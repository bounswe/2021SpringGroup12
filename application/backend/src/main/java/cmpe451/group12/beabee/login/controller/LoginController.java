package cmpe451.group12.beabee.login.controller;

import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
@RequestMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private final LoginService service;

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody UserCredentialsDTO userCredentialsDTO) throws Exception {
        return service.login(userCredentialsDTO);
    }


    @PostMapping("/signup")
    public MessageResponse signup(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        return service.signup(userCredentialsDTO);
    }
}
