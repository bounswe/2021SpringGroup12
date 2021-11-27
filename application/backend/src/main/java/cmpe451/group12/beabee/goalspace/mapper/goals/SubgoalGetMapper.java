package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubgoalGetMapper {


    SubgoalGetDTO mapToDto(Subgoal subgoal);

    Subgoal mapToEntity(SubgoalGetDTO subgoalGetDTO);

    List<SubgoalGetDTO> mapToDto(List<Subgoal> subgoalList);

    List<Subgoal> mapToEntity(List<SubgoalGetDTO> subgoalGetDTOList);

}
