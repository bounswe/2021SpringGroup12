//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class GoalGetMapper {
    public GoalGetMapper() {
    }

    public GoalGetDTO mapToDto(Goal goal) {
        if (goal == null) {
            return null;
        } else {
            GoalGetDTO goalGetDTO = new GoalGetDTO();
            goalGetDTO.setId(goal.getId());
            goalGetDTO.setGoalType(goal.getGoalType());
            goalGetDTO.setTitle(goal.getTitle());
            goalGetDTO.setIsDone(goal.getIsDone());
            goalGetDTO.setDescription(goal.getDescription());
            goalGetDTO.setCreatedAt(goal.getCreatedAt());
            goalGetDTO.setSubgoals(this.subgoalSetToSubgoalDTOShortSet(goal.getSubgoals()));
            goalGetDTO.setIsPublished(goal.getIsPublished());
            return goalGetDTO;
        }
    }

    public Goal mapToEntity(GoalGetDTO goalGetDTO) {
        if (goalGetDTO == null) {
            return null;
        } else {
            Goal goal = new Goal();
            goal.setGoalType(goalGetDTO.getGoalType());
            goal.setTitle(goalGetDTO.getTitle());
            goal.setIsDone(goalGetDTO.getIsDone());
            goal.setDescription(goalGetDTO.getDescription());
            goal.setCreatedAt(goalGetDTO.getCreatedAt());
            goal.setId(goalGetDTO.getId());
            goal.setSubgoals(this.subgoalDTOShortSetToSubgoalSet(goalGetDTO.getSubgoals()));
            goal.setIsPublished(goalGetDTO.getIsPublished());
            return goal;
        }
    }

    public List<GoalGetDTO> mapToDto(List<Goal> goalList) {
        if (goalList == null) {
            return null;
        } else {
            List<GoalGetDTO> list = new ArrayList(goalList.size());
            Iterator var3 = goalList.iterator();

            while(var3.hasNext()) {
                Goal goal = (Goal)var3.next();
                list.add(this.mapToDto(goal));
            }

            return list;
        }
    }

    public List<Goal> mapToEntity(List<GoalGetDTO> goalGetDTOList) {
        if (goalGetDTOList == null) {
            return null;
        } else {
            List<Goal> list = new ArrayList(goalGetDTOList.size());
            Iterator var3 = goalGetDTOList.iterator();

            while(var3.hasNext()) {
                GoalGetDTO goalGetDTO = (GoalGetDTO)var3.next();
                list.add(this.mapToEntity(goalGetDTO));
            }

            return list;
        }
    }

    protected SubgoalDTOShort subgoalToSubgoalDTOShort(Subgoal subgoal) {
        if (subgoal == null) {
            return null;
        } else {
            SubgoalDTOShort subgoalDTOShort = new SubgoalDTOShort();
            subgoalDTOShort.setId(subgoal.getId());
            subgoalDTOShort.setTitle(subgoal.getTitle());
            subgoalDTOShort.setDescription(subgoal.getDescription());
            return subgoalDTOShort;
        }
    }

    protected Set<SubgoalDTOShort> subgoalSetToSubgoalDTOShortSet(Set<Subgoal> set) {
        if (set == null) {
            return null;
        } else {
            Set<SubgoalDTOShort> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                Subgoal subgoal = (Subgoal)var3.next();
                set1.add(this.subgoalToSubgoalDTOShort(subgoal));
            }

            return set1;
        }
    }

    protected Subgoal subgoalDTOShortToSubgoal(SubgoalDTOShort subgoalDTOShort) {
        if (subgoalDTOShort == null) {
            return null;
        } else {
            Subgoal subgoal = new Subgoal();
            subgoal.setId(subgoalDTOShort.getId());
            subgoal.setTitle(subgoalDTOShort.getTitle());
            subgoal.setDescription(subgoalDTOShort.getDescription());
            return subgoal;
        }
    }

    protected Set<Subgoal> subgoalDTOShortSetToSubgoalSet(Set<SubgoalDTOShort> set) {
        if (set == null) {
            return null;
        } else {
            Set<Subgoal> set1 = new HashSet(Math.max((int)((float)set.size() / 0.75F) + 1, 16));
            Iterator var3 = set.iterator();

            while(var3.hasNext()) {
                SubgoalDTOShort subgoalDTOShort = (SubgoalDTOShort)var3.next();
                set1.add(this.subgoalDTOShortToSubgoal(subgoalDTOShort));
            }

            return set1;
        }
    }
}
