package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.QuestionDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    QuestionDTO mapToDto(Question question);

    Question mapToEntity(QuestionDTO questionDTO);

    List<QuestionDTO> mapToDto(List<Question> questionList);

    List<Question> mapToEntity(List<QuestionDTO> questionDTOList);


}
