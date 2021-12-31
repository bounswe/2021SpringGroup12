package cmpe451.group12.beabee.goalspace.controller;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.dto.UserGetDTO;
import cmpe451.group12.beabee.common.service.UserService;
import cmpe451.group12.beabee.goalspace.dto.analytics.UserAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v2/users")
public class UserController {

    private final UserService userService;
    private final GoalService goalService;


    @ApiOperation(value = "Get analytics of a user.")
    @GetMapping("/analytics/{user_id}")
    public UserAnalyticsDTO getAnalytics(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id) {
        return userService.getAnalytics(user_id);
    }

    @ApiOperation(value = "Search for users.")
    @GetMapping("/search/{query}")
    public List<UserGetDTO> searchUser(@PathVariable @ApiParam(value = "Search query.", example = "veyis") String query) {
        return userService.searchUser(query);
    }
    @ApiOperation(value = "Get the user with given id.")
    @GetMapping("/get/{id}")
    public UserGetDTO getUser(@PathVariable @ApiParam(value = "Search query.", example = "5") Long  id) {
        return userService.getUser(id);
    }

    @ApiOperation(value = "Follow a user with given id.")
    @PostMapping("/{userId}/follow/{targetId}")
    public MessageResponse followUser(@PathVariable @ApiParam(value = "Id of the user who follows the other user.", example = "5") Long userId,
                                      @PathVariable @ApiParam(value = "Id of the user that gets followed.", example = "5") Long targetId) {
        return userService.followUser(userId, targetId);
    }


    @ApiOperation(value = "Follow a user with given id.")
    @PostMapping("/{userId}/unfollow/{targetId}")
    public MessageResponse unfollowUser(@PathVariable @ApiParam(value = "Id of the user who unfollows the other user.", example = "5") Long userId,
                                      @PathVariable @ApiParam(value = "Id of the user that gets unfollowed.", example = "5") Long targetId) {
        return userService.unfollowUser(userId, targetId);
    }


    @ApiOperation(value = "Get followers of a user.")
    @GetMapping("/{userId}/followers")
    public List<UserGetDTO> getFollowers(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long userId) {
        return userService.getFollowers(userId);
    }


    @ApiOperation(value = "Get followings of a user.")
    @GetMapping("/{userId}/followings")
    public List<UserGetDTO> getFollowings(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long userId) {
        return userService.getFollowings(userId);
    }
}