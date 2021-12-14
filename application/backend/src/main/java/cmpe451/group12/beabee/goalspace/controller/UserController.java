package cmpe451.group12.beabee.goalspace.controller;

import cmpe451.group12.beabee.common.service.UserService;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.analytics.UserAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.service.GoalService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}