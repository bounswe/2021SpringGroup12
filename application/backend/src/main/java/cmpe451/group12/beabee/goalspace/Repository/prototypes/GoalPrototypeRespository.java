package cmpe451.group12.beabee.goalspace.Repository.prototypes;

import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface GoalPrototypeRespository extends JpaRepository<GoalPrototype, Long> {

    List<GoalPrototype> findAllByDescriptionContainsOrTitleContains(String query1, String query2);

    List<GoalPrototype> findAllByTagsIsContaining(Tag tag1);
    Set<GoalPrototype> findAllByHiddentagsIsContaining(Tag tag1);


}
