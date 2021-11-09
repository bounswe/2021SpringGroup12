package cmpe451.group12.beabee.login.repository;

import cmpe451.group12.beabee.login.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <Users, String> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
}
