package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalPostDTO;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupGoalPostMapper {
    //GroupGoalPostDTO mapToDto(GroupGoal groupGoal);

    public GroupGoal mapToEntity(GroupGoalPostDTO groupGoalPostDTO)
    {
        if ( groupGoalPostDTO == null ) {
            return null;
        }

        GroupGoal groupGoal = new GroupGoal();
        groupGoal.setTitle(groupGoalPostDTO.getTitle());
        groupGoal.setDescription(groupGoalPostDTO.getDescription());

        return groupGoal;
    }

    //List<GroupGoalPostDTO> mapToDto(List<GroupGoal> groupGoalList);

    public List<GroupGoal> mapToEntity(List<GroupGoalPostDTO> goalPostDTOList)
    {
        if ( goalPostDTOList == null ) {
            return null;
        }

        List<GroupGoal> list = new ArrayList<GroupGoal>( goalPostDTOList.size() );
        for ( GroupGoalPostDTO groupGoalPostDTO : goalPostDTOList ) {
            list.add( mapToEntity( groupGoalPostDTO ) );
        }

        return list;
    }

}
