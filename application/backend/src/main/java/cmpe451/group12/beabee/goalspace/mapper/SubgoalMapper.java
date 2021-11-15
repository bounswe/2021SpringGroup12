package cmpe451.group12.beabee.goalspace.mapper;

import cmpe451.group12.beabee.goalspace.dto.SubgoalDTO;
import cmpe451.group12.beabee.goalspace.model.Subgoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubgoalMapper {


    SubgoalDTO mapToDto(Subgoal subgoal);

    Subgoal mapToEntity(SubgoalDTO subgoalDTO);

    List<SubgoalDTO> mapToDto(List<Subgoal> subgoalList);

    List<Subgoal> mapToEntity(List<SubgoalDTO> subgoalDTOList);

}
