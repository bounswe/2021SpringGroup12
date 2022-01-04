
package cmpe451.group12.beabee.goalspace.mapper.entities;


import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class EntitiMapper {

    public EntitiMapper() {
    }


    public EntitiDTO mapToDto(Reflection reflection) {
        if (reflection == null) {
            return null;
        } else {
            EntitiDTO entityDTO = new EntitiDTO();
            entityDTO.setId(reflection.getId());
            entityDTO.setEntitiType(reflection.getEntitiType());
            entityDTO.setTitle(reflection.getTitle());
            entityDTO.setIsDone(reflection.getIsDone());
            entityDTO.setDescription(reflection.getDescription());
            entityDTO.setCreatedAt(reflection.getCreatedAt());
            return entityDTO;
        }
    }
    public EntitiDTO mapToDto(Question question) {
        if (question == null) {
            return null;
        } else {
            EntitiDTO entityDTO = new EntitiDTO();
            entityDTO.setId(question.getId());
            entityDTO.setEntitiType(question.getEntitiType());

            entityDTO.setTitle(question.getTitle());
            entityDTO.setIsDone(question.getIsDone());
            entityDTO.setDescription(question.getDescription());
            entityDTO.setCreatedAt(question.getCreatedAt());
            return entityDTO;
        }
    }
    public EntitiDTO mapToDto(Task task) {
        if (task == null) {
            return null;
        } else {
            EntitiDTO entityDTO = new EntitiDTO();
            entityDTO.setId(task.getId());
            entityDTO.setEntitiType(task.getEntitiType());

            entityDTO.setTitle(task.getTitle());
            entityDTO.setIsDone(task.getIsDone());
            entityDTO.setDescription(task.getDescription());
            entityDTO.setDeadline(task.getDeadline());
            entityDTO.setCreatedAt(task.getCreatedAt());
            entityDTO.setRating(task.getRating());
            return entityDTO;
        }
    }

    public EntitiDTO mapToDto(Routine routine) {
        if (routine == null) {
            return null;
        } else {
            EntitiDTO entityDTO = new EntitiDTO();
            entityDTO.setId(routine.getId());
            entityDTO.setEntitiType(routine.getEntitiType());

            entityDTO.setTitle(routine.getTitle());
            entityDTO.setIsDone(routine.getIsDone());
            entityDTO.setDescription(routine.getDescription());
            entityDTO.setDeadline(routine.getDeadline().get(routine.getDeadline().size()-1));
            entityDTO.setCreatedAt(routine.getCreatedAt());
            entityDTO.setRating(routine.getRating().stream().mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
            entityDTO.setPeriod(routine.getPeriod());
            return entityDTO;
        }
    }

    public List<EntitiDTO> mapToReflectionDto(List<Reflection> reflectionList) {
        if (reflectionList == null) {
            return null;
        } else {
            List<EntitiDTO> list = new ArrayList(reflectionList.size());
            Iterator var3 = reflectionList.iterator();

            while(var3.hasNext()) {
                Reflection reflection = (Reflection)var3.next();
                list.add(this.mapToDto(reflection));
            }

            return list;
        }
    }
    public List<EntitiDTO> mapToQuestionDto(List<Question> questionList) {
        if (questionList == null) {
            return null;
        } else {
            List<EntitiDTO> list = new ArrayList(questionList.size());
            Iterator var3 = questionList.iterator();

            while(var3.hasNext()) {
                Question question = (Question)var3.next();
                list.add(this.mapToDto(question));
            }

            return list;
        }
    }
    public List<EntitiDTO> mapToTaskDto(List<Task> taskList) {
        if (taskList == null) {
            return null;
        } else {
            List<EntitiDTO> list = new ArrayList(taskList.size());
            Iterator var3 = taskList.iterator();

            while(var3.hasNext()) {
                Task task = (Task)var3.next();
                list.add(this.mapToDto(task));
            }

            return list;
        }
    }
    public List<EntitiDTO> mapToRoutineDto(List<Routine> routineList) {
        if (routineList == null) {
            return null;
        } else {
            List<EntitiDTO> list = new ArrayList(routineList.size());
            Iterator var3 = routineList.iterator();

            while(var3.hasNext()) {
                Routine routine = (Routine)var3.next();
                list.add(this.mapToDto(routine));
            }
            return list;
        }
    }


}
