package cmpe451.group12.beabee.login.service;

import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.config.security.MyUserDetailsService;
import cmpe451.group12.beabee.login.dto.UserDTO;
import cmpe451.group12.beabee.login.mapper.UserMapper;
import cmpe451.group12.beabee.login.repository.UserRepository;
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
    private final UserMapper userMapper;

    public AuthenticationResponse createAuthenticationToken(UserDTO user) throws Exception
    {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Wrong username or password!", e);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(user,jwt,"Login successful!", MessageType.SUCCESS);
    }

    public MessageResponse signup(UserDTO user) {
        try { // TODO: prevent adding an existing user
            if(userRepository.findByUsername(user.getUsername()).isPresent()){
                Random rand = new Random();
                return new MessageResponse("Username is already in use. Try "+ user.getUsername()+"_"+ rand.nextInt(1000)+" instead?",MessageType.ERROR);
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new MessageResponse("Email address is already in use. You already have an account?", MessageType.INFO);
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(userMapper.mapToEntity(user));
        }catch (Exception e){
            System.out.println(e);
            return new MessageResponse("Couldn't sign up user!", MessageType.ERROR);
        }
        return new MessageResponse("User has signed up successfully!", MessageType.SUCCESS);
    }
}
