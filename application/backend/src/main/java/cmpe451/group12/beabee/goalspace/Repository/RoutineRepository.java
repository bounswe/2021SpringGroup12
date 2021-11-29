package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    Optional<Routine> findById(Long entity_id);

    @Query(value = "SELECT * FROM Routine WHERE goal_id = ?1",nativeQuery = true)
    List<Routine> findByGoalId(@Param("goal_id") Long goal_id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM routine WHERE id = ?1",nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
