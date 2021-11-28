package cmpe451.group12.beabee.goalspace.mapper.resources;

import cmpe451.group12.beabee.goalspace.dto.resources.ResourceDTOShort;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourceShortMapper {

    ResourceDTOShort mapToDto(Resource resource);

    Resource mapToEntity(ResourceDTOShort resourceDTOShort);

    List<ResourceDTOShort> mapToDto(List<Resource> resourceList);

    List<Resource> mapToEntity(List<ResourceDTOShort> resourceDTOShortList);
}
