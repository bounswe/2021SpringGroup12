package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupGoalShortMapper {
    GroupGoalDTOShort mapToDto(GroupGoal groupGoal);

    GroupGoal mapToEntity(GroupGoalDTOShort groupGoalDTOShort);

    List<GroupGoalDTOShort> mapToDto(List<GroupGoal> groupGoalList);

    List<GroupGoal> mapToEntity(List<GroupGoalDTOShort> groupGoalDTOShort);
}
