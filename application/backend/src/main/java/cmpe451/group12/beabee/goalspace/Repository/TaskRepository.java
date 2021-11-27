package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findById(Long entity_id);

    @Query(value = "SELECT * FROM Task WHERE goal_id = ?1",nativeQuery = true)
    List<Task> findByGoalId(@Param("goal_id") Long goal_id);

}
