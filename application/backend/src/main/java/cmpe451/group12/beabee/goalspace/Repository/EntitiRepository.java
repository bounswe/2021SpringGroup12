package cmpe451.group12.beabee.goalspace.Repository;

import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntitiRepository  extends JpaRepository<Entiti, Long> {

    Optional<Entiti> findById(Long entity_id);

    List<Entiti> findAllByMainGoal(Goal goal);



}
