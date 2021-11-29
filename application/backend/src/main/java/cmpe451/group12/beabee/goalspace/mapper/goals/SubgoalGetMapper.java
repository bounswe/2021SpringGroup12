//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.goals;

import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SubgoalGetMapper {
    public SubgoalGetMapper() {
    }

    public SubgoalGetDTO mapToDto(Subgoal subgoal) {
        if (subgoal == null) {
            return null;
        } else {
            SubgoalGetDTO subgoalGetDTO = new SubgoalGetDTO();
            subgoalGetDTO.setId(subgoal.getId());
            subgoalGetDTO.setTitle(subgoal.getTitle());
            subgoalGetDTO.setDescription(subgoal.getDescription());
            subgoalGetDTO.setCreatedAt(subgoal.getCreatedAt());
            subgoalGetDTO.setIsDone(subgoal.getIsDone());
            subgoalGetDTO.setDeadline(subgoal.getDeadline());
            subgoalGetDTO.setRating(subgoal.getRating());
            return subgoalGetDTO;
        }
    }

    public Subgoal mapToEntity(SubgoalGetDTO subgoalGetDTO) {
        if (subgoalGetDTO == null) {
            return null;
        } else {
            Subgoal subgoal = new Subgoal();
            subgoal.setId(subgoalGetDTO.getId());
            subgoal.setTitle(subgoalGetDTO.getTitle());
            subgoal.setIsDone(subgoalGetDTO.getIsDone());
            subgoal.setDescription(subgoalGetDTO.getDescription());
            subgoal.setCreatedAt(subgoalGetDTO.getCreatedAt());
            subgoal.setDeadline(subgoalGetDTO.getDeadline());
            subgoal.setRating(subgoalGetDTO.getRating());
            return subgoal;
        }
    }

    public List<SubgoalGetDTO> mapToDto(List<Subgoal> subgoalList) {
        if (subgoalList == null) {
            return null;
        } else {
            List<SubgoalGetDTO> list = new ArrayList(subgoalList.size());
            Iterator var3 = subgoalList.iterator();

            while(var3.hasNext()) {
                Subgoal subgoal = (Subgoal)var3.next();
                list.add(this.mapToDto(subgoal));
            }

            return list;
        }
    }

    public List<Subgoal> mapToEntity(List<SubgoalGetDTO> subgoalGetDTOList) {
        if (subgoalGetDTOList == null) {
            return null;
        } else {
            List<Subgoal> list = new ArrayList(subgoalGetDTOList.size());
            Iterator var3 = subgoalGetDTOList.iterator();

            while(var3.hasNext()) {
                SubgoalGetDTO subgoalGetDTO = (SubgoalGetDTO)var3.next();
                list.add(this.mapToEntity(subgoalGetDTO));
            }

            return list;
        }
    }
}
