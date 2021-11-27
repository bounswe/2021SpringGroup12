package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.RoutineDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoutineMapper {

    RoutineDTO mapToDto(Routine routine);

    Routine mapToEntity(RoutineDTO routineDTO);

    List<RoutineDTO> mapToDto(List<Routine> routineList);

    List<Routine> mapToEntity(List<RoutineDTO> routineDTOList);


}
