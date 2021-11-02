package cmpe451.group12.beabee.repository;

import cmpe451.group12.beabee.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <Users, String> {
    Optional<Users> findByUsername(String username);

}
