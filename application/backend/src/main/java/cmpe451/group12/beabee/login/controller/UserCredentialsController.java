package cmpe451.group12.beabee.login.controller;

import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.service.UserCredentialsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserCredentialsController {

    private final UserCredentialsService userCredentialsService;

    @GetMapping("/{username}")
    public UserCredentialsDTO getUser(@PathVariable String username) {
        return userCredentialsService.getUserByUsername(username);
    }

}
