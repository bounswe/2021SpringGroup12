package cmpe451.group12.beabee.login.service;


import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {

    private  final UserRepository userRepository;
    private  final  UserCredentialsMapper userCredentialsMapper;


    public UserCredentialsDTO getUserByUsername(String username) {
        Optional<Users> user = userRepository.findByUsername(username);

       if(user.isPresent()){
           user.get().setPassword("***");
           return userCredentialsMapper.mapToDto(user.get());
       }
        return new UserCredentialsDTO();
    }
}
