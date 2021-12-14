//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.QuestionGetDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.ReflectionGetDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class QuestionGetMapper {
    public QuestionGetMapper() {
    }

    public QuestionGetDTO mapToDto(Question question) {
        if (question == null) {
            return null;
        } else {
            QuestionGetDTO questionGetDTO = new QuestionGetDTO();
            questionGetDTO.setId(question.getId());
            questionGetDTO.setEntitiType(question.getEntitiType());
            questionGetDTO.setTitle(question.getTitle());
            questionGetDTO.setDescription(question.getDescription());
            questionGetDTO.setCreatedAt(question.getCreatedAt());
            questionGetDTO.setIsDone(question.getIsDone());
            return questionGetDTO;
        }
    }

    public Question mapToEntity(QuestionGetDTO questionGetDTO) {
        if (questionGetDTO == null) {
            return null;
        } else {
            Question question = new Question();
            question.setId(questionGetDTO.getId());
            question.setEntitiType(questionGetDTO.getEntitiType());
            question.setTitle(questionGetDTO.getTitle());
            question.setDescription(questionGetDTO.getDescription());
            question.setIsDone(questionGetDTO.getIsDone());
            question.setCreatedAt(questionGetDTO.getCreatedAt());
            return question;
        }
    }

    public List<QuestionGetDTO> mapToDto(List<Question> questionList) {
        if (questionList == null) {
            return null;
        } else {
            List<QuestionGetDTO> list = new ArrayList(questionList.size());
            Iterator var3 = questionList.iterator();

            while(var3.hasNext()) {
                Question question = (Question)var3.next();
                list.add(this.mapToDto(question));
            }

            return list;
        }
    }

    public List<Question> mapToEntity(List<QuestionGetDTO> questionGetDTOList) {
        if (questionGetDTOList == null) {
            return null;
        } else {
            List<Question> list = new ArrayList(questionGetDTOList.size());
            Iterator var3 = questionGetDTOList.iterator();

            while(var3.hasNext()) {
                QuestionGetDTO questionGetDTO = (QuestionGetDTO)var3.next();
                list.add(this.mapToEntity(questionGetDTO));
            }
            return list;
        }
    }
}
