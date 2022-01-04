package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GoalShortMapper {

    GoalDTOShort mapToDto(Goal goal);

    Goal mapToEntity(GoalDTOShort goalDTOShort);

    List<GoalDTOShort> mapToDto(List<Goal> goalList);

    List<Goal> mapToEntity(List<GoalDTOShort> goalDTOShortList);

}
