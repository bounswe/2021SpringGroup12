package cmpe451.group12.beabee.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.model.Users;
import cmpe451.group12.beabee.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(EntityNotFoundException::new);
    }

    public MessageResponse addUser(Users user) {
        try { // TODO: prevent adding an existing user
            System.out.println(user.toString());
            userRepository.save(user);
        }catch (Exception e){
            System.out.println(e);
            return new MessageResponse("Couldn't add user!", MessageType.ERROR);
        }
        return new MessageResponse("User has been added successfully!", MessageType.SUCCESS);
    }
}
