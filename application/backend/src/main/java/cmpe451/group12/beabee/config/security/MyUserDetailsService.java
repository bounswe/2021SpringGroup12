package cmpe451.group12.beabee.config.security;

import cmpe451.group12.beabee.login.model.Users;
import cmpe451.group12.beabee.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user =  userRepository.findByUsername(username);
        if (user.isPresent()){
            return new User(user.get().getUsername(),user.get().getPassword(),new ArrayList<>());
            //return user.get();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }
}