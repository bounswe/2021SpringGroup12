package cmpe451.group12.beabee.login.mapper;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.login.dto.UserCredentialsPostDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserCredentialsPostMapper {

    UserCredentialsPostDTO mapToDto(Users user);

    Users mapToEntity(UserCredentialsPostDTO userCredentialsPostDTO);

    List<UserCredentialsPostDTO> mapToDto(List<Users> userCredentialsList);

    List<Users> mapToEntity(List<UserCredentialsPostDTO> userCredentialsPostDTOList);

}
