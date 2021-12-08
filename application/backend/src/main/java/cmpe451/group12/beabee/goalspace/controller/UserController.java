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

import java.util.Date;

@CrossOrigin(origins = "http://localhost:8085")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(value = "/v2/users")
public class UserController {

    private final UserService userService;
    private final GoalService goalService;


    @ApiOperation(value = "Get analytics of a user.")
    @GetMapping(value = {"/analytics/{user_id}", "/analytics/{user_id}/{days_before}"})
    public UserAnalyticsDTO getAnalytics(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long user_id,
                                         @PathVariable(required = false) @ApiParam(value = "How many days before the report will start. It's optional. If not present, report will start from the beginning.", example = "5") Long days_before) {
        return userService.getAnalytics(user_id, days_before);
    }
}
