package cmpe451.group12.beabee.controller;

import cmpe451.group12.beabee.common.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.config.security.MyUserDetailsService;
import cmpe451.group12.beabee.mapper.UserMapper;
import cmpe451.group12.beabee.model.Users;
import cmpe451.group12.beabee.repository.UserRepository;
import cmpe451.group12.beabee.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody Users user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Wrong username or password!", e);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(userMapper.mapToDto(user),jwt,"Login successful!",MessageType.SUCCESS);
    }


    @PostMapping("/signup")
    public MessageResponse signup(@RequestBody  Users user) {
        try { // TODO: prevent adding an existing user
            if(userRepository.findByUsername(user.getUsername()).isPresent()){
                Random rand = new Random();
                return new MessageResponse("Username is already in use. Try "+ user.getUsername()+"_"+ rand.nextInt(1000)+" instead?",MessageType.ERROR);
            }
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return new MessageResponse("Email address is already in use. You already have an account?", MessageType.INFO);
            }
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
        }catch (Exception e){
            System.out.println(e);
            return new MessageResponse("Couldn't sign up user!", MessageType.ERROR);
        }
        return new MessageResponse("User has signed up successfully!", MessageType.SUCCESS);
    }
}
