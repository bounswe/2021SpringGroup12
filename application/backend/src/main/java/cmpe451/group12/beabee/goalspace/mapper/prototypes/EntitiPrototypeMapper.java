package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTO;
import cmpe451.group12.beabee.goalspace.model.prototypes.EntitiPrototype;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface EntitiPrototypeMapper {
    EntitiPrototypeDTO mapToDto(EntitiPrototype entitiPrototype);

    EntitiPrototype mapToEntity(EntitiPrototypeDTO prototypeDTO);

    Set<EntitiPrototypeDTO> mapToDto(Set<EntitiPrototype> entitiPrototypes);

    Set<EntitiPrototype> mapToEntity(Set<EntitiPrototypeDTO> entitiPrototypeDTOS);

}
