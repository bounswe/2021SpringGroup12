package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.ReflectionPostDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReflectionPostMapper {
    ReflectionPostDTO mapToDto(Reflection reflection);

    Reflection mapToEntity(ReflectionPostDTO reflectionDTO);

    List<ReflectionPostDTO> mapToDto(List<Reflection> reflectionList);

    List<Reflection> mapToEntity(List<ReflectionPostDTO> reflectionDTOList);

}
