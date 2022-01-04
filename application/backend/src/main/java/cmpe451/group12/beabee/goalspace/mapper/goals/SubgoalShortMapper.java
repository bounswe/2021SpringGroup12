package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubgoalShortMapper {

    SubgoalDTOShort mapToDto(Subgoal subgoal);

    Subgoal mapToEntity(SubgoalDTOShort subgoalDTOShort);

    List<SubgoalDTOShort> mapToDto(List<Subgoal> subgoalList);

    List<Subgoal> mapToEntity(List<SubgoalDTOShort> subgoalDTOShortList);

}
