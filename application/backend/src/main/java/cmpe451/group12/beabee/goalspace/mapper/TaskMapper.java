package cmpe451.group12.beabee.goalspace.mapper;

import cmpe451.group12.beabee.goalspace.dto.TaskDTO;
import cmpe451.group12.beabee.goalspace.model.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {


    TaskDTO mapToDto(Task task);

    Task mapToEntity(TaskDTO taskDTO);

    List<TaskDTO> mapToDto(List<Task> taskList);

    List<Task> mapToEntity(List<TaskDTO> taskDTOList);

}
