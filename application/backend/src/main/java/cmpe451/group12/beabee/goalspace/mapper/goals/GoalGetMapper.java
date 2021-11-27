package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalGetMapper {
    GoalGetDTO mapToDto(Goal goal);

    Goal mapToEntity(GoalGetDTO goalGetDTO);

    List<GoalGetDTO> mapToDto(List<Goal> goalList);

    List<Goal> mapToEntity(List<GoalGetDTO> goalGetDTOList);

}
