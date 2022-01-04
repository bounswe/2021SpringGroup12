package cmpe451.group12.beabee.login.service;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import cmpe451.group12.beabee.login.dto.UserCredentialsPostDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsGetMapper;
import cmpe451.group12.beabee.login.mapper.UserCredentialsPostMapper;
import cmpe451.group12.beabee.login.util.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {

    private  final UserRepository userRepository;
    private  final UserCredentialsGetMapper userCredentialsGetMapper;
    private final EmailSender emailSender;


    public UserCredentialsGetDTO getUserByUsername(String username) {
        Optional<Users> user = userRepository.findByUsername(username);

       if(user.isPresent()){
           user.get().setPassword("***");
           return userCredentialsGetMapper.mapToDto(user.get());
       }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
    }

    public MessageResponse sendResetPasswordMail(String email_or_username) throws MessagingException {
        Optional<Users> user = userRepository.findByEmail(email_or_username);
        if (user.isEmpty())
            user = userRepository.findByUsername(email_or_username);
        if (user.isPresent()) {
            Random random = new Random();
            String new_reset_token = random.ints(48, 123)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(10)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            user.get().setPassword_reset_token(new_reset_token);
            user.get().setPassword_reset_token_expiration_date(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)));
            emailSender.send(user.get());

            userRepository.save(user.get());
            return new MessageResponse("Please check your e-mail address: " + user.get().getEmail().substring(0,2) + "***" + user.get().getEmail().substring(user.get().getEmail().indexOf('@') - 2, user.get().getEmail().indexOf('@')) + "@**", MessageType.INFO);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    public MessageResponse resetPassword(UserCredentialsPostDTO userDTO, String resetToken) {
        Optional<Users> user = userRepository.findByUsername(userDTO.getUsername());
        if (user.isPresent()) {
            Date now = new Date(System.currentTimeMillis());
            if (user.get().getPassword_reset_token().equals(resetToken)) { // reset token is correct
                Random random = new Random();
                if (now.getTime() - user.get().getPassword_reset_token_expiration_date().getTime() > TimeUnit.MINUTES.toMillis(5)) { // time expired
                    String new_reset_token = random.ints(48, 123)
                            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                            .limit(10)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    user.get().setPassword_reset_token(new_reset_token);
                    user.get().setPassword_reset_token_expiration_date(new Date(new Date(System.currentTimeMillis()).getTime()+ TimeUnit.MINUTES.toMillis(5)));
                    userRepository.save(user.get());
                    return new MessageResponse("Token expired!", MessageType.ERROR);
                } else {//all good
                    String new_reset_token = random.ints(48, 123)
                            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                            .limit(10)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    user.get().setPassword_reset_token(new_reset_token);
                    user.get().setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

                    userRepository.save(user.get());
                    return new MessageResponse("Password changed successfully!", MessageType.SUCCESS);
                }
            } else {
                return new MessageResponse("Token does not match?", MessageType.ERROR);
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
    }


}
