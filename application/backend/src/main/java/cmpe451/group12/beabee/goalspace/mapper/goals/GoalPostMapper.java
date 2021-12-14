package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalPostMapper {

    GoalPostDTO mapToDto(Goal goal);

    Goal mapToEntity(GoalPostDTO goalPostDTO);

    List<GoalPostDTO> mapToDto(List<Goal> goalList);

    List<Goal> mapToEntity(List<GoalPostDTO> goalPostDTOList);

}
