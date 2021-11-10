package cmpe451.group12.beabee.common.mapper;

import cmpe451.group12.beabee.goalspace.dto.UserDTO;
import cmpe451.group12.beabee.common.model.Users;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO mapToDto(Users user);

    Users mapToEntity(UserDTO userCredentialsDTO);

    List<UserDTO> mapToDto(List<Users> userCredentialsList);

    List<Users> mapToEntity(List<UserDTO> userCredentialsDTOList);

}
