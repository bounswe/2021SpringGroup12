package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.model.prototypes.SubgoalPrototype;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SubgoalPrototypeMapper {
    SubgoalPrototypeDTO mapToDto(SubgoalPrototype goal);

    SubgoalPrototype mapToEntity(SubgoalPrototypeDTO goalPrototype);

    Set<SubgoalPrototypeDTO> mapToDto(Set<SubgoalPrototype> goalList);

    Set<SubgoalPrototype> mapToEntity(Set<SubgoalPrototypeDTO> goalPrototypeList);

}
