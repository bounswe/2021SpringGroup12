package cmpe451.group12.beabee.common.service;


import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalPostMapper;
import cmpe451.group12.beabee.common.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final UserMapper userMapper;
    private final GoalPostMapper goalPostMapper;

    public UserGetDTO getUserById(Long id) {
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
