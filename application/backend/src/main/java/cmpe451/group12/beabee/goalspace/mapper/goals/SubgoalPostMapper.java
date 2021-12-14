package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubgoalPostMapper {


    SubgoalPostDTO mapToDto(Subgoal subgoal);

    Subgoal mapToEntity(SubgoalPostDTO subgoalPostDTO);

    List<SubgoalPostDTO> mapToDto(List<Subgoal> subgoalList);

    List<Subgoal> mapToEntity(List<SubgoalPostDTO> subgoalPostDTOList);

}
