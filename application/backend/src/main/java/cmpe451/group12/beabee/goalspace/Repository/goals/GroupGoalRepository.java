package cmpe451.group12.beabee.goalspace.Repository.goals;

import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface GroupGoalRepository extends JpaRepository<GroupGoal, Long>
{
    Optional<GroupGoal> findById(Long ID);

    Optional<GroupGoal> findByToken(String token);

    @Query(value = "SELECT * FROM GROUP_GOAL WHERE user_id = ?1",nativeQuery = true)
    List<GroupGoal> findAllByUserId(@Param("user_id") Long user_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM GROUP_GOAL WHERE id = ?1", nativeQuery = true)
    void deleteAGroupgoal(@Param("goal_id")  Long goal_id);
}
