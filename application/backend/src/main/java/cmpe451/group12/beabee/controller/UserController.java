package cmpe451.group12.beabee.controller;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.dto.UserDTO;
import cmpe451.group12.beabee.service.UserService;
import cmpe451.group12.beabee.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{username}")
    public UserDTO getUser(@PathVariable String username) {
        return userMapper.mapToDto(userService.getUserByUsername(username));
    }

}
