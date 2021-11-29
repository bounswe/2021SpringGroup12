//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.ReflectionGetDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReflectionGetMapper {
    public ReflectionGetMapper() {
    }

    public ReflectionGetDTO mapToDto(Reflection reflection) {
        if (reflection == null) {
            return null;
        } else {
            ReflectionGetDTO reflectionGetDTO = new ReflectionGetDTO();
            reflectionGetDTO.setId(reflection.getId());
            reflectionGetDTO.setEntitiType(reflection.getEntitiType());
            reflectionGetDTO.setTitle(reflection.getTitle());
            reflectionGetDTO.setDescription(reflection.getDescription());
            reflectionGetDTO.setCreatedAt(reflection.getCreatedAt());
            reflectionGetDTO.setIsDone(reflection.getIsDone());
            return reflectionGetDTO;
        }
    }

    public Reflection mapToEntity(ReflectionGetDTO reflectionGetDTO) {
        if (reflectionGetDTO == null) {
            return null;
        } else {
            Reflection reflection = new Reflection();
            reflection.setId(reflectionGetDTO.getId());
            reflection.setEntitiType(reflectionGetDTO.getEntitiType());
            reflection.setTitle(reflectionGetDTO.getTitle());
            reflection.setDescription(reflectionGetDTO.getDescription());
            reflection.setIsDone(reflectionGetDTO.getIsDone());
            reflection.setCreatedAt(reflectionGetDTO.getCreatedAt());
            return reflection;
        }
    }

    public List<ReflectionGetDTO> mapToDto(List<Reflection> reflectionList) {
        if (reflectionList == null) {
            return null;
        } else {
            List<ReflectionGetDTO> list = new ArrayList(reflectionList.size());
            Iterator var3 = reflectionList.iterator();

            while(var3.hasNext()) {
                Reflection reflection = (Reflection)var3.next();
                list.add(this.mapToDto(reflection));
            }

            return list;
        }
    }

    public List<Reflection> mapToEntity(List<ReflectionGetDTO> reflectionGetDTOList) {
        if (reflectionGetDTOList == null) {
            return null;
        } else {
            List<Reflection> list = new ArrayList(reflectionGetDTOList.size());
            Iterator var3 = reflectionGetDTOList.iterator();

            while(var3.hasNext()) {
                ReflectionGetDTO reflectionGetDTO = (ReflectionGetDTO)var3.next();
                list.add(this.mapToEntity(reflectionGetDTO));
            }

            return list;
        }
    }
}
