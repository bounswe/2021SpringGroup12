package cmpe451.group12.beabee.login.controller;

import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.login.dto.UserDTO;
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
    public AuthenticationResponse createAuthenticationToken(@RequestBody UserDTO user) throws Exception {
        return service.createAuthenticationToken(user);
    }


    @PostMapping("/signup")
    public MessageResponse signup(@RequestBody UserDTO user) {
        return service.signup(user);
    }
}
