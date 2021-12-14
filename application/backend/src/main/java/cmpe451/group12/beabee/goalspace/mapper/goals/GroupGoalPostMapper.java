package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupGoalPostMapper {
    GroupGoalPostDTO mapToDto(GroupGoal groupGoal);

    GroupGoal mapToEntity(GroupGoalPostDTO groupGoalPostDTO);

    List<GroupGoalPostDTO> mapToDto(List<GroupGoal> groupGoalList);

    List<GroupGoal> mapToEntity(List<GroupGoalPostDTO> goalPostDTOList);

}
