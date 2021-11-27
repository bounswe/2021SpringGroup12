package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.RoutinePostDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoutinePostMapper {
    RoutinePostDTO mapToDto(Routine routine);

    Routine mapToEntity(RoutinePostDTO routinePostDTO);

    List<RoutinePostDTO> mapToDto(List<Routine> routineList);

    List<Routine> mapToEntity(List<RoutinePostDTO> routinePostDTOList);
}
