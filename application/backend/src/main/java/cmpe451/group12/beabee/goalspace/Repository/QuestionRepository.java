package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long entity_id);


    @Query(value = "SELECT * FROM Question WHERE goal_id = ?1",nativeQuery = true)
    List<Question> findByGoalId(@Param("goal_id") Long goal_id);
}
