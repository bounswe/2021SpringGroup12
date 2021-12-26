package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTOShort;
import cmpe451.group12.beabee.goalspace.model.prototypes.SubgoalPrototype;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface SubgoalPrototypeShortMapper {
    SubgoalPrototypeDTOShort mapToDto(SubgoalPrototype subgoalPrototype);

    SubgoalPrototype mapToEntity(SubgoalPrototypeDTOShort subgoalPrototypeDTOShort);

    Set<SubgoalPrototypeDTOShort> mapToDto(Set<SubgoalPrototype> subgoalPrototypes);

    Set<SubgoalPrototype> mapToEntity(Set<SubgoalPrototypeDTOShort> subgoalPrototypeDTOShorts);
}
