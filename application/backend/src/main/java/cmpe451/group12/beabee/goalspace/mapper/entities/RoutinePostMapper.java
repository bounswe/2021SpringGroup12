//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.RoutinePostDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

@Component
public class RoutinePostMapper{
    public RoutinePostMapper() {
    }

    public RoutinePostDTO mapToDto(Routine routine) {
        if (routine == null) {
            return null;
        } else {
            RoutinePostDTO routinePostDTO = new RoutinePostDTO();
            routinePostDTO.setTitle(routine.getTitle());
            routinePostDTO.setDescription(routine.getDescription());
            routinePostDTO.setDeadline(routine.getDeadline().get(routine.getDeadline().size()-1));
            routinePostDTO.setPeriod(routine.getPeriod());
            return routinePostDTO;
        }
    }

    public Routine mapToEntity(RoutinePostDTO routinePostDTO) {
        if (routinePostDTO == null) {
            return null;
        } else {
            Routine routine = new Routine();
            routine.setTitle(routinePostDTO.getTitle());
            routine.setDescription(routinePostDTO.getDescription());
            routine.setPeriod(routinePostDTO.getPeriod());
            routine.setDeadline(Stream.of(routinePostDTO.getDeadline()).collect(Collectors.toList()));
            return routine;
        }
    }

    public List<RoutinePostDTO> mapToDto(List<Routine> routineList) {
        if (routineList == null) {
            return null;
        } else {
            List<RoutinePostDTO> list = new ArrayList(routineList.size());
            Iterator var3 = routineList.iterator();

            while(var3.hasNext()) {
                Routine routine = (Routine)var3.next();
                list.add(this.mapToDto(routine));
            }

            return list;
        }
    }

    public List<Routine> mapToEntity(List<RoutinePostDTO> routinePostDTOList) {
        if (routinePostDTOList == null) {
            return null;
        } else {
            List<Routine> list = new ArrayList(routinePostDTOList.size());
            Iterator var3 = routinePostDTOList.iterator();

            while(var3.hasNext()) {
                RoutinePostDTO routinePostDTO = (RoutinePostDTO)var3.next();
                list.add(this.mapToEntity(routinePostDTO));
            }

            return list;
        }
    }
}
