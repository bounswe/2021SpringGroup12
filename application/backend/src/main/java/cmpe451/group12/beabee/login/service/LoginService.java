package cmpe451.group12.beabee.login.service;

import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.config.security.MyUserDetailsService;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsMapper;
import cmpe451.group12.beabee.login.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoginService
{
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserCredentialsMapper userCredentialsMapper;

    public AuthenticationResponse login(UserCredentialsDTO userCredentialsDTO) throws Exception
    {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Wrong username or password!", e);
        }
        userCredentialsDTO.setId(userRepository.findByUsername(userCredentialsDTO.getUsername()).get().getUser_id());
        userCredentialsDTO.setPassword("***");
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(userCredentialsDTO.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(userCredentialsDTO,jwt,"Login successful!", MessageType.SUCCESS);
    }

    public MessageResponse signup(UserCredentialsDTO userCredentialsDTO) {
        try {
            if(userRepository.findByUsername(userCredentialsDTO.getUsername()).isPresent()){
                Random rand = new Random();
                return new MessageResponse("Username is already in use. Try "+ userCredentialsDTO.getUsername()+"_"+ rand.nextInt(1000)+" instead?",MessageType.ERROR);
            }
            if (userRepository.findByEmail(userCredentialsDTO.getEmail()).isPresent()) {
                return new MessageResponse("Email address is already in use. You already have an account?", MessageType.INFO);
            }
            Users new_user = userCredentialsMapper.mapToEntity(userCredentialsDTO);
            new_user.setPassword(new BCryptPasswordEncoder().encode(userCredentialsDTO.getPassword()));
            userRepository.save(new_user);
        }catch (Exception e){
            System.out.println(e);
            return new MessageResponse("Couldn't sign up user!", MessageType.ERROR);
        }
        return new MessageResponse("User has signed up successfully!", MessageType.SUCCESS);
    }
}
