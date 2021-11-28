
package cmpe451.group12.beabee.goalspace.mapper.entities;


import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class EntitiShortMapper {

    public EntitiShortMapper() {
    }


    public EntitiDTOShort mapToDto(Reflection reflection) {
        if (reflection == null) {
            return null;
        } else {
            EntitiDTOShort entityDTO = new EntitiDTOShort();
            entityDTO.setId(reflection.getId());
            entityDTO.setEntitiType(reflection.getEntitiType());
            entityDTO.setTitle(reflection.getTitle());
            entityDTO.setDescription(reflection.getDescription());
            return entityDTO;
        }
    }
    public EntitiDTOShort mapToDto(Question question) {
        if (question == null) {
            return null;
        } else {
            EntitiDTOShort entityDTO = new EntitiDTOShort();
            entityDTO.setId(question.getId());
            entityDTO.setEntitiType(question.getEntitiType());
            entityDTO.setTitle(question.getTitle());
            entityDTO.setDescription(question.getDescription());

            return entityDTO;
        }
    }
    public EntitiDTOShort mapToDto(Task task) {
        if (task == null) {
            return null;
        } else {
            EntitiDTOShort entityDTO = new EntitiDTOShort();
            entityDTO.setId(task.getId());
            entityDTO.setEntitiType(task.getEntitiType());
            entityDTO.setTitle(task.getTitle());
            entityDTO.setDescription(task.getDescription());
            entityDTO.setDeadline(task.getDeadline());
            return entityDTO;
        }
    }

    public EntitiDTOShort mapToDto(Routine routine) {
        if (routine == null) {
            return null;
        } else {
            EntitiDTOShort entityDTO = new EntitiDTOShort();
            entityDTO.setId(routine.getId());
            entityDTO.setEntitiType(routine.getEntitiType());

            entityDTO.setTitle(routine.getTitle());
            entityDTO.setDescription(routine.getDescription());
            entityDTO.setDeadline(routine.getDeadline());

            return entityDTO;
        }
    }

    public List<EntitiDTOShort> mapToReflectionDto(List<Reflection> reflectionList) {
        if (reflectionList == null) {
            return null;
        } else {
            List<EntitiDTOShort> list = new ArrayList(reflectionList.size());
            Iterator var3 = reflectionList.iterator();

            while(var3.hasNext()) {
                Reflection reflection = (Reflection)var3.next();
                list.add(this.mapToDto(reflection));
            }

            return list;
        }
    }
    public List<EntitiDTOShort> mapToQuestionDto(List<Question> questionList) {
        if (questionList == null) {
            return null;
        } else {
            List<EntitiDTOShort> list = new ArrayList(questionList.size());
            Iterator var3 = questionList.iterator();

            while(var3.hasNext()) {
                Question question = (Question)var3.next();
                list.add(this.mapToDto(question));
            }

            return list;
        }
    }
    public List<EntitiDTOShort> mapToTaskDto(List<Task> taskList) {
        if (taskList == null) {
            return null;
        } else {
            List<EntitiDTOShort> list = new ArrayList(taskList.size());
            Iterator var3 = taskList.iterator();

            while(var3.hasNext()) {
                Task task = (Task)var3.next();
                list.add(this.mapToDto(task));
            }

            return list;
        }
    }
    public List<EntitiDTOShort> mapToRoutineDto(List<Routine> routineList) {
        if (routineList == null) {
            return null;
        } else {
            List<EntitiDTOShort> list = new ArrayList(routineList.size());
            Iterator var3 = routineList.iterator();

            while(var3.hasNext()) {
                Routine routine = (Routine)var3.next();
                list.add(this.mapToDto(routine));
            }
            return list;
        }
    }


}
