package cmpe451.group12.beabee.common.mapper;

import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// Auto generated mapper did not work for USERS, so I implemented it myself.
@Component
public class UserMapper {
    public UserGetDTO mapToDto(Users user) {
        return UserGetDTO.builder()
                .id(user.getUser_id())
                .followerCount(((long) user.getFollowers().size()))
                .followingCount((long) user.getFollowing().size())
                .userCredentials(UserCredentialsGetDTO.builder()
                        .user_id(user.getUser_id())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .password("****")
                        .build())
                .build();
    }

    // TODO Do we really need this for GetDTO
    public Users mapToEntity(UserGetDTO userCredentialsDTO) {
        return new Users();
    }

    public List<UserGetDTO> mapToDto(List<Users> userCredentialsList) {
        return userCredentialsList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<Users> mapToEntity(List<UserGetDTO> userCredentialsDTOList) {
        return userCredentialsDTOList.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }

}
