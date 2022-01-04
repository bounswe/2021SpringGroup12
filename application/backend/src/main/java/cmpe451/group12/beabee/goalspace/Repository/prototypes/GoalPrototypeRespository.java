package cmpe451.group12.beabee.goalspace.Repository.prototypes;

import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GoalPrototypeRespository extends JpaRepository<GoalPrototype, Long> {

    List<GoalPrototype> findAllByDescriptionContainsOrTitleContains(String query1, String query2);

    List<GoalPrototype> findAllByTagsIsContaining(Tag tag1);
    Set<GoalPrototype> findAllByHiddentagsIsContaining(Tag tag1);

    @Query(value = "SELECT * FROM goal_prototype WHERE reference_id =?1",nativeQuery = true)
    Optional<GoalPrototype> findByReference_goal_id(@Param("ref_goal") Long ref_goal);
}
