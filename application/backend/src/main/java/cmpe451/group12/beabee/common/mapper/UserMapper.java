package cmpe451.group12.beabee.common.mapper;

import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.common.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserGetDTO mapToDto(Users user);

    Users mapToEntity(UserGetDTO userCredentialsDTO);

    List<UserGetDTO> mapToDto(List<Users> userCredentialsList);

    List<Users> mapToEntity(List<UserGetDTO> userCredentialsDTOList);

}
