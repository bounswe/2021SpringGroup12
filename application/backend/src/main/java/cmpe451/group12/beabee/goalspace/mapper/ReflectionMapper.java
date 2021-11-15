package cmpe451.group12.beabee.goalspace.mapper;

import cmpe451.group12.beabee.goalspace.dto.ReflectionDTO;
import cmpe451.group12.beabee.goalspace.model.Reflection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReflectionMapper {


    ReflectionDTO mapToDto(Reflection reflection);

    Reflection mapToEntity(ReflectionDTO reflectionDTO);

    List<ReflectionDTO> mapToDto(List<Reflection> reflectionList);

    List<Reflection> mapToEntity(List<ReflectionDTO> reflectionDTOList);

}
