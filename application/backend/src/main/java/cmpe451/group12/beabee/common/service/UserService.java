package cmpe451.group12.beabee.common.service;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.goalspace.dto.GoalDTO;
import cmpe451.group12.beabee.goalspace.dto.UserDTO;
import cmpe451.group12.beabee.goalspace.mapper.GoalMapper;
import cmpe451.group12.beabee.common.mapper.UserMapper;
import cmpe451.group12.beabee.goalspace.model.Entiti;
import cmpe451.group12.beabee.goalspace.model.Goal;
import cmpe451.group12.beabee.common.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final UserMapper userMapper;
    private final GoalMapper goalMapper;

    public UserDTO getUserById(Long id) {
        return userMapper.mapToDto(userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }
    /*
    public List<UserDTO> getFollowersList(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new).getFollowers().stream().map(x -> userMapper.mapToDto(x)).collect(Collectors.toList());
    }

    public List<UserDTO> getFollowingList(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new).getFollowing().stream().map(x -> userMapper.mapToDto(x)).collect(Collectors.toList());
    }
*/


    /*
    public MessageResponse followAUser(Long my_id, Long target_id) {
        Users this_user = userRepository.findById(my_id).orElseThrow(EntityNotFoundException::new);
        Users target_user = userRepository.findById(target_id).orElseThrow(EntityNotFoundException::new);

        Set<Users> this_user_following_list =  userRepository.findById(my_id).orElseThrow(EntityNotFoundException::new).getFollowing();
        // TODO: this may be problematic , I might have to implement custom equals class for users
        if(this_user_following_list.contains(target_user)){
            return new MessageResponse("Already following that user!", MessageType.INFO);
        }
        this_user_following_list.add(target_user);
        Set<Users> target_users_follower_list = target_user.getFollowers();
        target_users_follower_list.add(this_user);
        userRepository.save(target_user);
        userRepository.save(this_user);
        return new MessageResponse("User followed.", MessageType.SUCCESS);
    }
    */
}
