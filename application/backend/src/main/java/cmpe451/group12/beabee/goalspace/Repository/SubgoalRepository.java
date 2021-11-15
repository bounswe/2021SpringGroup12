package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Question;
import cmpe451.group12.beabee.goalspace.model.Subgoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubgoalRepository extends JpaRepository<Subgoal, Long> {

    Optional<Subgoal> findById(Long entity_id);

    @Query(value = "SELECT * FROM Subgoal WHERE goal_id = ?1",nativeQuery = true)
    List<Subgoal> findByGoalId(@Param("goal_id") Long goal_id);

}
