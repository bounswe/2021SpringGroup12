package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findById(Long ID);

    @Query(value = "SELECT * FROM GOAL WHERE user_id = ?1",nativeQuery = true)
    List<Goal> findAllByUserId(@Param("user_id") Long user_id);

    @Query(value = "select max(id) from goal", nativeQuery = true)
    Long lastId();


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Goal WHERE id = ?1", nativeQuery = true)
    void deleteAGoal(@Param("goal_id")  Long goal_id);
}
