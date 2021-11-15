package cmpe451.group12.beabee.goalspace.mapper;

import cmpe451.group12.beabee.goalspace.dto.GoalDTO;
import cmpe451.group12.beabee.goalspace.model.Goal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    GoalDTO mapToDto(Goal goal);

    Goal mapToEntity(GoalDTO goalDTO);

    List<GoalDTO> mapToDto(List<Goal> goalList);

    List<Goal> mapToEntity(List<GoalDTO> goalDTOList);

}
