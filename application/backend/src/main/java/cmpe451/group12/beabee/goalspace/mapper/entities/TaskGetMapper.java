//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.TaskGetDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TaskGetMapper/*Impl implements TaskGetMapper*/ {
    public TaskGetMapper/*Impl*/() {
    }

    public TaskGetDTO mapToDto(Task task) {
        if (task == null) {
            return null;
        } else {
            TaskGetDTO taskGetDTO = new TaskGetDTO();
            taskGetDTO.setId(task.getId());
            taskGetDTO.setEntitiType(task.getEntitiType());
            taskGetDTO.setTitle(task.getTitle());
            taskGetDTO.setDescription(task.getDescription());
            taskGetDTO.setCreatedAt(task.getCreatedAt());
            taskGetDTO.setDeadline(task.getDeadline());
            taskGetDTO.setIsDone(task.getIsDone());
            taskGetDTO.setRating(task.getRating());
            return taskGetDTO;
        }
    }

    public Task mapToEntity(TaskGetDTO taskGetDTO) {
        if (taskGetDTO == null) {
            return null;
        } else {
            Task task = new Task();
            task.setId(taskGetDTO.getId());
            task.setEntitiType(taskGetDTO.getEntitiType());
            task.setTitle(taskGetDTO.getTitle());
            task.setDescription(taskGetDTO.getDescription());
            task.setIsDone(taskGetDTO.getIsDone());
            task.setCreatedAt(taskGetDTO.getCreatedAt());
            task.setDeadline(taskGetDTO.getDeadline());
            task.setRating(taskGetDTO.getRating());
            return task;
        }
    }

    public List<TaskGetDTO> mapToDto(List<Task> taskList) {
        if (taskList == null) {
            return null;
        } else {
            List<TaskGetDTO> list = new ArrayList(taskList.size());
            Iterator var3 = taskList.iterator();

            while(var3.hasNext()) {
                Task task = (Task)var3.next();
                list.add(this.mapToDto(task));
            }

            return list;
        }
    }

    public List<Task> mapToEntity(List<TaskGetDTO> taskGetDTOList) {
        if (taskGetDTOList == null) {
            return null;
        } else {
            List<Task> list = new ArrayList(taskGetDTOList.size());
            Iterator var3 = taskGetDTOList.iterator();

            while(var3.hasNext()) {
                TaskGetDTO taskGetDTO = (TaskGetDTO)var3.next();
                list.add(this.mapToEntity(taskGetDTO));
            }

            return list;
        }
    }
}
