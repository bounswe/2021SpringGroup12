package cmpe451.group12.beabee.goalspace.Repository.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface SubgoalRepository extends JpaRepository<Subgoal, Long> {

    Optional<Subgoal> findById(Long entity_id);

    @Query(value = "SELECT * FROM Subgoal WHERE main_goal_id = ?1",nativeQuery = true)
    List<Subgoal> findByGoalId(@Param("goal_id") Long goal_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM subgoal WHERE id = ?1",nativeQuery = true)
    void deleteFromSubgoalById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = " DELETE FROM subgoal WHERE id IN (SELECT child_subgoals_id FROM subgoals WHERE subgoal_id = ?1 ) ; ",nativeQuery = true)
    void deleteChildsById(@Param("id") Long id);

    @Query(value = "SELECT * FROM subgoal WHERE id IN (SELECT subgoal_id FROM subgoals WHERE child_subgoals_id = ?1)",nativeQuery = true)
    Subgoal findParentById(@Param("id") Long id);

    List<Subgoal> findAllByCreator(Users user);
}
