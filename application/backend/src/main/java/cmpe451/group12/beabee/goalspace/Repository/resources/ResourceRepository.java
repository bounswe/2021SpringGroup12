package cmpe451.group12.beabee.goalspace.Repository.resources;

import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Boolean existsByNameAndEntiti(String name, Entiti entiti);



    @Modifying
    @Transactional
    @Query(value = "DELETE FROM resource WHERE id = ?1",nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
