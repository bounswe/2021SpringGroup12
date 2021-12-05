package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalGetDto;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GroupGoalGetMapper {
    public GroupGoalGetMapper() {
    }

    public GroupGoalGetDto mapToDto(GroupGoal groupGoal) {
        if (groupGoal == null) {
            return null;
        } else {
            GroupGoalGetDto groupgoalGetDTO = new GroupGoalGetDto();
            groupgoalGetDTO.setId(groupGoal.getId());
            groupgoalGetDTO.setGoalType(groupGoal.getGoalType());
            groupgoalGetDTO.setTitle(groupGoal.getTitle());
            groupgoalGetDTO.setIsDone(groupGoal.getIsDone());
            groupgoalGetDTO.setDescription(groupGoal.getDescription());
            groupgoalGetDTO.setDeadline(groupGoal.getDeadline());
            groupgoalGetDTO.setCreatedAt(groupGoal.getCreatedAt());
            groupgoalGetDTO.setSubgoals(this.subgoalSetToSubgoalDTOShortSet(groupGoal.getSubgoals()));
            groupgoalGetDTO.setToken(groupGoal.getToken());
            groupgoalGetDTO.setMembers(
                    groupGoal.getMembers().stream()
                            .map(Users::getUser_id)
                            .collect(Collectors.toSet())
            );
            return groupgoalGetDTO;
        }
    }

    public List<GroupGoalGetDto> mapToDto(List<GroupGoal> groupGoalList) {
        if (groupGoalList == null) {
            return null;
        } else {
            List<GroupGoalGetDto> list = new ArrayList(groupGoalList.size());
            Iterator var3 = groupGoalList.iterator();

            while(var3.hasNext()) {
                GroupGoal groupGoal = (GroupGoal)var3.next();
                list.add(this.mapToDto(groupGoal));
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
            subgoalDTOShort.setDeadline(subgoal.getDeadline());
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
            subgoal.setDeadline(subgoalDTOShort.getDeadline());
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
