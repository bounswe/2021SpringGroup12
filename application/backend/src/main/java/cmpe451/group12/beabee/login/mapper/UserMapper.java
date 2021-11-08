package cmpe451.group12.beabee.login.mapper;

import cmpe451.group12.beabee.login.dto.UserDTO;
import cmpe451.group12.beabee.login.model.Users;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO mapToDto(Users user);

    Users mapToEntity(UserDTO userDTO);

    List<UserDTO> mapToDto(List<Users> usersList);

    List<Users> mapToEntity(List<UserDTO> userDTOList);

}
