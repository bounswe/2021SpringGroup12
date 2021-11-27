package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReflectionRepository extends JpaRepository<Reflection, Long> {

    Optional<Reflection> findById(Long reflection_id);

    @Query(value = "SELECT * FROM Reflection WHERE goal_id = ?1",nativeQuery = true)
    List<Reflection> findByGoalId(@Param("goal_id") Long goal_id);

}
