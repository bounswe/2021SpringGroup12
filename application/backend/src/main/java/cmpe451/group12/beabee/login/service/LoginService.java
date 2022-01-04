package cmpe451.group12.beabee.login.service;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.login.dto.AuthenticationResponse;
import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.config.security.MyUserDetailsService;

import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import cmpe451.group12.beabee.login.dto.UserCredentialsPostDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsGetMapper;
import cmpe451.group12.beabee.login.mapper.UserCredentialsPostMapper;
import cmpe451.group12.beabee.login.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoginService
{
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserCredentialsGetMapper userCredentialsGetMapper;
    private final UserCredentialsPostMapper userCredentialsPostMapper;

    public AuthenticationResponse login(UserCredentialsPostDTO userCredentialsPostDTO) throws Exception
    {
        Users user = userCredentialsPostMapper.mapToEntity(userCredentialsPostDTO);
        if (user.getUsername().equals("")){
            user = userRepository.findByEmail(userCredentialsPostDTO.getEmail()).get();
        }
        UserCredentialsGetDTO userCredentialsGetDTO = userCredentialsGetMapper.mapToDto(user);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userCredentialsPostDTO.getUsername(), userCredentialsPostDTO.getPassword()));
        } catch (BadCredentialsException e) {
            return new AuthenticationResponse(userCredentialsGetDTO,"","Wrong username or password!",MessageType.ERROR);
            //throw new Exception("Wrong username or password!", e);
        }
        userCredentialsGetDTO.setUser_id(userRepository.findByUsername(userCredentialsGetDTO.getUsername()).get().getUser_id());
        userCredentialsGetDTO.setPassword("***");
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(userCredentialsGetDTO.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(userCredentialsGetDTO,jwt,"Login successful!", MessageType.SUCCESS);
    }

    public MessageResponse signup(UserCredentialsPostDTO userCredentialsPostDTO) {
        try {
            if(userRepository.findByUsername(userCredentialsPostDTO.getUsername()).isPresent()){
                Random rand = new Random();
                return new MessageResponse("Username is already in use. Try "+ userCredentialsPostDTO.getUsername()+"_"+ rand.nextInt(1000)+" instead?",MessageType.ERROR);
            }
            if (userRepository.findByEmail(userCredentialsPostDTO.getEmail()).isPresent()) {
                return new MessageResponse("Email address is already in use. You already have an account?", MessageType.INFO);
            }
            Users new_user = userCredentialsPostMapper.mapToEntity(userCredentialsPostDTO);
            new_user.setPassword(new BCryptPasswordEncoder().encode(userCredentialsPostDTO.getPassword()));
            userRepository.save(new_user);
        }catch (Exception e){
            System.out.println(e);
            return new MessageResponse("Couldn't sign up user!", MessageType.ERROR);
        }
        return new MessageResponse("User has signed up successfully!", MessageType.SUCCESS);
    }
}
