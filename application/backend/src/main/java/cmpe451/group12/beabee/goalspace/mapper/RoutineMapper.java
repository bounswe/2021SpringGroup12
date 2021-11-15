package cmpe451.group12.beabee.goalspace.mapper;

import cmpe451.group12.beabee.goalspace.dto.RoutineDTO;
import cmpe451.group12.beabee.goalspace.model.Routine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoutineMapper {

    RoutineDTO mapToDto(Routine routine);

    Routine mapToEntity(RoutineDTO routineDTO);

    List<RoutineDTO> mapToDto(List<Routine> routineList);

    List<Routine> mapToEntity(List<RoutineDTO> routineDTOList);


}
