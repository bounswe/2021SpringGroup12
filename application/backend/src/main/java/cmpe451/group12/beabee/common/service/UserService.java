package cmpe451.group12.beabee.common.service;

import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.UserAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalPostMapper;
import cmpe451.group12.beabee.common.mapper.UserMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.GoalShortMapper;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;

import cmpe451.group12.beabee.goalspace.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.UserAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;

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

    public UserGetDTO getUserById(Long id) {
        return userMapper.mapToDto(userRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public UserAnalyticsDTO getAnalytics(Long user_id, Long days_before) {
        if (days_before!= null && days_before<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filter parameter must be positive!");
        }
        Date beginning =  new Date(System.currentTimeMillis() - System.currentTimeMillis());

        if(days_before != null){
            beginning =  new Date(System.currentTimeMillis() - days_before * 24 * 3600 * 1000);
        }
        UserAnalyticsDTO userAnalyticsDTO = new UserAnalyticsDTO();
        Date finalBeginning = beginning;
        List<Goal> goals = goalRepository.findAllByUserId(user_id).stream().filter(x->x.getCreatedAt().after(finalBeginning)).collect(Collectors.toList());
        if (goals.size() == 0) {
            //this user has no goal yet
            userAnalyticsDTO.setActiveGoalCount(0L);
            userAnalyticsDTO.setCompletedGoalCount(0L);
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


}
