package cmpe451.group12.beabee.common.repository;

import cmpe451.group12.beabee.common.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(Long ID);

//    @Query(value = "SELECT * FROM USERS WHERE username = ?1",nativeQuery = true)
  //  Optional<Users> findByUsername(@Param("username") String username);
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);


}
