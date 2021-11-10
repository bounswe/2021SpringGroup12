package cmpe451.group12.beabee.login.mapper;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.login.dto.UserCredentialsDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserCredentialsMapper {

    UserCredentialsDTO mapToDto(Users user);

    Users mapToEntity(UserCredentialsDTO userCredentialsDTO);

    List<UserCredentialsDTO> mapToDto(List<Users> userCredentialsList);

    List<Users> mapToEntity(List<UserCredentialsDTO> userCredentialsDTOList);

}
