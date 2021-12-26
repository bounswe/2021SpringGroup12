package cmpe451.group12.beabee.goalspace.Repository.goals;

import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findByName(String name);
}
