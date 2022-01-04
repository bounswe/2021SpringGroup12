package cmpe451.group12.beabee.goalspace.Repository.goals;

import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, String> {
    Set<Tag> findAllByNameContains(String name);
    List<Tag> findByName(String name);
}
