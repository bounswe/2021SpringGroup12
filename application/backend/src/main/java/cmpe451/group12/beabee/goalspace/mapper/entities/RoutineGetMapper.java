//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.RoutineGetDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoutineGetMapper   {
    public RoutineGetMapper() {
    }

    public RoutineGetDTO mapToDto(Routine routine) {
        if (routine == null) {
            return null;
        } else {
            RoutineGetDTO routineGetDTO = new RoutineGetDTO();
            routineGetDTO.setId(routine.getId());
            routineGetDTO.setEntitiType(routine.getEntitiType());
            routineGetDTO.setTitle(routine.getTitle());
            routineGetDTO.setDescription(routine.getDescription());
            routineGetDTO.setCreatedAt(routine.getCreatedAt());
            routineGetDTO.setIsDone(routine.getIsDone());
            routineGetDTO.setDeadline(routine.getDeadline());
            routineGetDTO.setPeriod(routine.getPeriod());
            return routineGetDTO;
        }
    }

    public Routine mapToEntity(RoutineGetDTO routineGetDTO) {
        if (routineGetDTO == null) {
            return null;
        } else {
            Routine routine = new Routine();
            routine.setId(routineGetDTO.getId());
            routine.setEntitiType(routineGetDTO.getEntitiType());
            routine.setTitle(routineGetDTO.getTitle());
            routine.setDescription(routineGetDTO.getDescription());
            routine.setIsDone(routineGetDTO.getIsDone());
            routine.setCreatedAt(routineGetDTO.getCreatedAt());
            routine.setPeriod(routineGetDTO.getPeriod());
            routine.setDeadline(routineGetDTO.getDeadline());
            return routine;
        }
    }

    public List<RoutineGetDTO> mapToDto(List<Routine> routineList) {
        if (routineList == null) {
            return null;
        } else {
            List<RoutineGetDTO> list = new ArrayList(routineList.size());
            Iterator var3 = routineList.iterator();

            while(var3.hasNext()) {
                Routine routine = (Routine)var3.next();
                list.add(this.mapToDto(routine));
            }

            return list;
        }
    }

    public List<Routine> mapToEntity(List<RoutineGetDTO> routineGetDTOList) {
        if (routineGetDTOList == null) {
            return null;
        } else {
            List<Routine> list = new ArrayList(routineGetDTOList.size());
            Iterator var3 = routineGetDTOList.iterator();

            while(var3.hasNext()) {
                RoutineGetDTO routineGetDTO = (RoutineGetDTO)var3.next();
                list.add(this.mapToEntity(routineGetDTO));
            }

            return list;
        }
    }
}
