package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTOShort;
import cmpe451.group12.beabee.goalspace.model.prototypes.EntitiPrototype;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface EntitiPrototypeShortMapper {
    EntitiPrototypeDTOShort mapToDto(EntitiPrototype entitiPrototype);

    EntitiPrototype mapToEntity(EntitiPrototypeDTOShort entitiPrototypeDTOShort);

    Set<EntitiPrototypeDTOShort> mapToDto(Set<EntitiPrototype> entitiPrototypes);

    Set<EntitiPrototype> mapToEntity(Set<EntitiPrototypeDTOShort> entitiPrototypeDTOShorts);
}
