//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.prototypes;

import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTO;
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
/*
    protected Set<SubgoalPrototypeDTO> subgoalPrototypeSetToSubgoalPrototypeDTOSet(Set<SubgoalPrototype> set) {
        if (set == null) {
            return null;
        } else {
            Set<SubgoalPrototypeDTO> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                SubgoalPrototype subgoalPrototype = (SubgoalPrototype)var3.next();
                set1.add(this.subgoalPrototypeToSubgoalPrototypeDTO(subgoalPrototype));
            }

            return set1;
        }
    }

    protected SubgoalPrototypeDTO subgoalPrototypeToSubgoalPrototypeDTO(SubgoalPrototype subgoalPrototype) {
        if (subgoalPrototype == null) {
            return null;
        } else {
            SubgoalPrototypeDTO subgoalPrototypeDTO = new SubgoalPrototypeDTO();
            subgoalPrototypeDTO.setId(subgoalPrototype.getId());
            subgoalPrototypeDTO.setReference_subgoal_id(subgoalPrototype.getReference_subgoal_id());
            subgoalPrototypeDTO.setTitle(subgoalPrototype.getTitle());
            subgoalPrototypeDTO.setDescription(subgoalPrototype.getDescription());
            subgoalPrototypeDTO.setChild_subgoals(this.subgoalPrototypeSetToSubgoalPrototypeDTOSet(subgoalPrototype.getChild_subgoals()));
            return subgoalPrototypeDTO;
        }
    }

    protected Set<SubgoalPrototype> subgoalPrototypeDTOSetToSubgoalPrototypeSet(Set<SubgoalPrototypeDTO> set) {
        if (set == null) {
            return null;
        } else {
            Set<SubgoalPrototype> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                SubgoalPrototypeDTO subgoalPrototypeDTO = (SubgoalPrototypeDTO)var3.next();
                set1.add(this.subgoalPrototypeDTOToSubgoalPrototype(subgoalPrototypeDTO));
            }

            return set1;
        }
    }

    protected SubgoalPrototype subgoalPrototypeDTOToSubgoalPrototype(SubgoalPrototypeDTO subgoalPrototypeDTO) {
        if (subgoalPrototypeDTO == null) {
            return null;
        } else {
            SubgoalPrototype subgoalPrototype = new SubgoalPrototype();
            subgoalPrototype.setId(subgoalPrototypeDTO.getId());
            subgoalPrototype.setReference_subgoal_id(subgoalPrototypeDTO.getReference_subgoal_id());
            subgoalPrototype.setTitle(subgoalPrototypeDTO.getTitle());
            subgoalPrototype.setDescription(subgoalPrototypeDTO.getDescription());
            subgoalPrototype.setChild_subgoals(this.subgoalPrototypeDTOSetToSubgoalPrototypeSet(subgoalPrototypeDTO.getChild_subgoals()));
            return subgoalPrototype;
        }
    }




    protected Set<EntitiPrototypeDTO> entitiPrototypeSetToEntitiPrototypeDTOSet(Set<EntitiPrototype> set) {
        if (set == null) {
            return null;
        } else {
            Set<EntitiPrototypeDTO> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                EntitiPrototype entitiPrototype = (EntitiPrototype)var3.next();
                set1.add(this.entitiPrototypeToEntitiPrototypeDTO(entitiPrototype));
            }
            return set1;
        }
    }

    protected EntitiPrototypeDTO entitiPrototypeToEntitiPrototypeDTO(EntitiPrototype entitiPrototype) {
        if (entitiPrototype == null) {
            return null;
        } else {
            EntitiPrototypeDTO entitiPrototypeDTO = new EntitiPrototypeDTO();
            entitiPrototypeDTO.setId(entitiPrototype.getId());
            entitiPrototypeDTO.setReferenced_entiti_id(entitiPrototype.getReference_entiti_id());
            entitiPrototypeDTO.setTitle(entitiPrototype.getTitle());
            entitiPrototypeDTO.setDescription(entitiPrototype.getDescription());
            entitiPrototypeDTO.setEntitiType(entitiPrototype.getEntitiType());
            entitiPrototypeDTO.setChild_entities(this.entitiPrototypeSetToEntitiPrototypeDTOSet(entitiPrototype.getChildEntities()));
            return entitiPrototypeDTO;
        }
    }

    protected Set<EntitiPrototype> entitiPrototypeDTOSetToEntitiPrototypeSet(Set<EntitiPrototypeDTO> set) {
        if (set == null) {
            return null;
        } else {
            Set<EntitiPrototype> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                EntitiPrototypeDTO entitiPrototypeDTO = (EntitiPrototypeDTO)var3.next();
                set1.add(this.entitiPrototypeDTOToEntitiPrototype(entitiPrototypeDTO));
            }

            return set1;
        }
    }

    protected EntitiPrototype entitiPrototypeDTOToEntitiPrototype(EntitiPrototypeDTO entitiPrototypeDTO) {
        if (entitiPrototypeDTO == null) {
            return null;
        } else {
            EntitiPrototype entitiPrototype = new EntitiPrototype();
            entitiPrototype.setId(entitiPrototypeDTO.getId());
            entitiPrototype.setReference_entiti_id(entitiPrototypeDTO.getReferenced_entiti_id());
            entitiPrototype.setTitle(entitiPrototypeDTO.getTitle());
            entitiPrototype.setDescription(entitiPrototypeDTO.getDescription());
            entitiPrototype.setChildEntities(this.entitiPrototypeDTOSetToEntitiPrototypeSet(entitiPrototypeDTO.getChild_entities()));
            entitiPrototype.setEntitiType(entitiPrototypeDTO.getEntitiType());
            return entitiPrototype;
        }
    }
*/
}