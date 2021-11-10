package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.GoalMapper;
import cmpe451.group12.beabee.goalspace.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findById(Long ID);

    @Query(value = "SELECT * FROM GOAL WHERE creator_id = ?1",nativeQuery = true)
    List<Goal> findAllByCreatorId(@Param("creator_id") Long creator_id);

    @Query(value = "select max(id) from goal", nativeQuery = true)
    Long lastId();

    @Transactional
    @Modifying
    @Query(value = " INSERT INTO GOAL(id,created_at, deadline, description, goal_type, is_done, title, creator_id) VALUES(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery = true)
    void saveManually(long id, Date createdAt, Date deadline, String description, Integer goalType, Boolean isDone, String title, Long creator_id);

}
