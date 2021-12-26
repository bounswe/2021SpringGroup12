package cmpe451.group12.beabee.goalspace.Repository.prototypes;

import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalPrototypeRespository extends JpaRepository<GoalPrototype, Long> {
}
