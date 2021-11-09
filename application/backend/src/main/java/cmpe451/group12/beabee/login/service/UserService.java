package cmpe451.group12.beabee.login.service;

import cmpe451.group12.beabee.login.dto.UserDTO;
import cmpe451.group12.beabee.login.mapper.UserMapper;
import cmpe451.group12.beabee.login.model.Users;
import cmpe451.group12.beabee.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUserByUsername(String username) {
        return userMapper.mapToDto(userRepository.findByUsername(username)
                .orElseThrow(EntityNotFoundException::new));
    }


}
