package cmpe451.group12.beabee.common.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.mapper.UserMapper;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.UserAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalPostMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalShortMapper;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.service.ActivityStreamService;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final UserMapper userMapper;
    private final GoalPostMapper goalPostMapper;
    private final GoalShortMapper goalShortMapper;
    private final GoalService goalService;
    private final ActivityStreamService activityStreamService;

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

    public UserAnalyticsDTO getAnalytics(Long user_id) {
        UserAnalyticsDTO userAnalyticsDTO = new UserAnalyticsDTO();
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        if (goals.size() == 0) {
            //this user has no goal yet
            return userAnalyticsDTO;
        }
        List<GoalAnalyticsDTO> goalAnalyticsDTOs = goals.stream().map(x -> goalService.getAnalytics(x.getId())).collect(Collectors.toList());

        userAnalyticsDTO.setAverageExtensionCount((long) goalAnalyticsDTOs.stream().map(x -> x.getExtensionCount()).mapToLong(Long::longValue).summaryStatistics().getAverage());
        userAnalyticsDTO.setAverageRating(goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.COMPLETED)).map(x -> x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
        userAnalyticsDTO.setAverageCompletionTimeOfGoalsInMiliseconds((long) goalAnalyticsDTOs.stream().filter(x -> x.getCompletionTimeInMiliseconds() != null).map(x -> x.getCompletionTimeInMiliseconds()).mapToLong(Long::longValue).summaryStatistics().getAverage());

        // set completion time of ACTIVE goals to now, so that we can calculate the time from the beginning to find out longest and shortest goals
        goalAnalyticsDTOs.stream().filter(x -> x.getCompletionTimeInMiliseconds() == null).forEach(x -> x.setCompletionTimeInMiliseconds(new Date(System.currentTimeMillis()).getTime() - x.getStartTime().getTime()));
        userAnalyticsDTO.setLongestGoal(goalShortMapper.mapToDto(goalRepository.findById(goalAnalyticsDTOs.stream().max(Comparator.comparing(GoalAnalyticsDTO::getCompletionTimeInMiliseconds)).get().getGoal_id()).get()));
        userAnalyticsDTO.setShortestGoal(goalShortMapper.mapToDto(goalRepository.findById(goalAnalyticsDTOs.stream().min(Comparator.comparing(GoalAnalyticsDTO::getCompletionTimeInMiliseconds)).get().getGoal_id()).get()));

        if (goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.COMPLETED)).count() > 0) {
            userAnalyticsDTO.setBestGoal(goalShortMapper.mapToDto(goalRepository.findById(goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.COMPLETED)).max(Comparator.comparing(GoalAnalyticsDTO::getRating)).get().getGoal_id()).get()));
            userAnalyticsDTO.setWorstGoal(goalShortMapper.mapToDto(goalRepository.findById(goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.COMPLETED)).min(Comparator.comparing(GoalAnalyticsDTO::getRating)).get().getGoal_id()).get()));
        }
        userAnalyticsDTO.setActiveGoalCount(goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.ACTIVE)).count());
        userAnalyticsDTO.setCompletedGoalCount(goalAnalyticsDTOs.stream().filter(x -> x.getStatus().equals(GoalAnalyticsDTO.Status.COMPLETED)).count());

        return userAnalyticsDTO;
    }


    public List<UserGetDTO> searchUser(String query) {
        List<UserGetDTO> ret = new ArrayList<>();
        List<Users> all_users = userRepository.findAllByUsernameIsContaining(query);
        all_users.stream().forEach(x ->{
            UserGetDTO userGetDTO = new UserGetDTO();
            UserCredentialsGetDTO userCredentialsGetDTO = new UserCredentialsGetDTO();
            userCredentialsGetDTO.setUsername(x.getUsername());
            userCredentialsGetDTO.setEmail(x.getEmail());
            userCredentialsGetDTO.setPassword(x.getPassword());
            userGetDTO.setUserCredentials(userCredentialsGetDTO);
            userGetDTO.setId(x.getUser_id());
            ret.add(userGetDTO);
        });
        return ret;

    }

    public UserGetDTO getUser(Long id) {
        return userMapper.mapToDto(userRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found!")));
    }

    public MessageResponse followUser(Long userId, Long targetUserId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Users targetUser = userRepository.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);

        if(userId == targetUserId){
            return new MessageResponse("You can't follow yourself!", MessageType.ERROR);
        }
        if (user.getFollowing().stream().anyMatch(following -> following.getUser_id().equals(targetUser.getUser_id()))) {
            return new MessageResponse("Already following!", MessageType.ERROR);
        }

        user.getFollowing().add(targetUser);
        targetUser.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(targetUser);

        activityStreamService.followUserSchema(user, targetUser);

        return new MessageResponse("User followed successfully!", MessageType.SUCCESS);
    }

    public MessageResponse unfollowUser(Long userId, Long targetUserId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        Users targetUser = userRepository.findById(targetUserId)
                .orElseThrow(EntityNotFoundException::new);

        if (user.getFollowing().stream().noneMatch(following -> following.getUser_id().equals(targetUser.getUser_id()))) {
            return new MessageResponse("Not following!", MessageType.ERROR);
        }

        user.getFollowing().remove(targetUser);
        targetUser.getFollowers().remove(user);

        userRepository.save(user);
        userRepository.save(targetUser);
// TODO Activity Stream type unfollow does not exist needs to think about it
//        activityStreamService.unfollowUserSchema(user, targetUser);

        return new MessageResponse("User unfollowed successfully!", MessageType.SUCCESS);
    }

    public List<UserGetDTO> getFollowers(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new)
                .getFollowers()
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserGetDTO> getFollowings(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new)
                .getFollowing()
                .stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
