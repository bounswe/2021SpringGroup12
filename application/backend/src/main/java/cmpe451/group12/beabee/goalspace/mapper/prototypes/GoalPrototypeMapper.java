//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTOShort;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.goalspace.model.prototypes.EntitiPrototype;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import cmpe451.group12.beabee.goalspace.model.prototypes.SubgoalPrototype;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class GoalPrototypeMapper {
    public GoalPrototypeMapper() {
    }

    public GoalPrototypeDTO mapToDto(GoalPrototype goalPrototype) {
        if (goalPrototype == null) {
            return null;
        } else {
            GoalPrototypeDTO goalPrototypeDTO = new GoalPrototypeDTO();
            goalPrototypeDTO.setId(goalPrototype.getId());
            goalPrototypeDTO.setReference_goal_id(goalPrototype.getReference_goal_id());
            goalPrototypeDTO.setTitle(goalPrototype.getTitle());
            goalPrototypeDTO.setDescription(goalPrototype.getDescription());
            Set<Tag> set2 = goalPrototype.getTags();
            if (set2 != null) {
                goalPrototypeDTO.setTags(set2.stream().map(x->x.getName()).collect(Collectors.toSet()));
            }
            return goalPrototypeDTO;
        }
    }

    public GoalPrototype mapToEntity(GoalPrototypeDTO prototypeDTO) {
        if (prototypeDTO == null) {
            return null;
        } else {
            GoalPrototype goalPrototype = new GoalPrototype();
            goalPrototype.setId(prototypeDTO.getId());
            goalPrototype.setReference_goal_id(prototypeDTO.getReference_goal_id());
            goalPrototype.setTitle(prototypeDTO.getTitle());
            goalPrototype.setDescription(prototypeDTO.getDescription());
            Set<Tag> set2 = Collections.emptySet();

            return goalPrototype;
        }
    }

    public List<GoalPrototypeDTO> mapToDto(List<GoalPrototype> goalPrototypeList) {
        if (goalPrototypeList == null) {
            return null;
        } else {
            List<GoalPrototypeDTO> list = new ArrayList(goalPrototypeList.size());
            Iterator var3 = goalPrototypeList.iterator();

            while(var3.hasNext()) {
                GoalPrototype goalPrototype = (GoalPrototype)var3.next();
                list.add(this.mapToDto(goalPrototype));
            }

            return list;
        }
    }

    public List<GoalPrototype> mapToEntity(List<GoalPrototypeDTO> goalPrototypeDTOS) {
        if (goalPrototypeDTOS == null) {
            return null;
        } else {
            List<GoalPrototype> list = new ArrayList(goalPrototypeDTOS.size());
            Iterator var3 = goalPrototypeDTOS.iterator();

            while(var3.hasNext()) {
                GoalPrototypeDTO goalPrototypeDTO = (GoalPrototypeDTO)var3.next();
                list.add(this.mapToEntity(goalPrototypeDTO));
            }

            return list;
        }
    }


}