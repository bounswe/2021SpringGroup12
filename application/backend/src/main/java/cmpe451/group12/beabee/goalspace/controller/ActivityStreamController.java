package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.goalspace.model.activitystreams.ActivitySchema;
import cmpe451.group12.beabee.goalspace.service.ActivityStreamService;
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
@RequestMapping(value = "/v2/activitystreams")
public class ActivityStreamController {
    private final ActivityStreamService activityStreamService;
    @ApiOperation(value = "Get all activities of a user.")
    @GetMapping("/{userId}")
    public List<ActivitySchema> getSchemasOfAUser(@PathVariable @ApiParam(value = "Id of the user.", example = "5") Long userId) {
        return activityStreamService.getSchemasOfAUser(userId);

    }
}
