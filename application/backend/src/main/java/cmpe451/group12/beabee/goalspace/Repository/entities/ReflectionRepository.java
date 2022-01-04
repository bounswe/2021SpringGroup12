package cmpe451.group12.beabee.goalspace.Repository.entities;

import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ReflectionRepository extends JpaRepository<Reflection, Long> {

    Optional<Reflection> findById(Long reflection_id);

    @Query(value = "SELECT * FROM Reflection WHERE goal_id = ?1",nativeQuery = true)
    List<Reflection> findByGoalId(@Param("goal_id") Long goal_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Reflection WHERE id = ?1",nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
