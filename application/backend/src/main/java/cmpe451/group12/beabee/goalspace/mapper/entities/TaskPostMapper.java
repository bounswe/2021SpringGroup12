package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.TaskPostDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskPostMapper {

    TaskPostDTO mapToDto(Task task);

    Task mapToEntity(TaskPostDTO taskDTO);

    List<TaskPostDTO> mapToDto(List<Task> taskList);

    List<Task> mapToEntity(List<TaskPostDTO> taskDTOList);

}
