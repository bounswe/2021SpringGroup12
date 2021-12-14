package cmpe451.group12.beabee.login.mapper;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserCredentialsGetMapper {

    UserCredentialsGetDTO mapToDto(Users user);

    Users mapToEntity(UserCredentialsGetDTO userCredentialsGetDTO);

    List<UserCredentialsGetDTO> mapToDto(List<Users> userCredentialsList);

    List<Users> mapToEntity(List<UserCredentialsGetDTO> userCredentialsGetDTOList);

}
