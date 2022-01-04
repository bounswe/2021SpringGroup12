package cmpe451.group12.beabee.goalspace.mapper.entities;

import cmpe451.group12.beabee.goalspace.dto.entities.QuestionPostDTO;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionPostMapper {
    QuestionPostDTO mapToDto(Question question);

    Question mapToEntity(QuestionPostDTO questionDTO);

    List<QuestionPostDTO> mapToDto(List<Question> questionList);

    List<Question> mapToEntity(List<QuestionPostDTO> questionDTOList);

}
