package cmpe451.group12.beabee.goalspace.Repository.entities;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntitiRepository  extends JpaRepository<Entiti, Long> {

    Optional<Entiti> findById(Long entity_id);

    List<Entiti> findAllByGoal(Goal goal);

    List<Entiti> findAllByCreator(Users user);


}
