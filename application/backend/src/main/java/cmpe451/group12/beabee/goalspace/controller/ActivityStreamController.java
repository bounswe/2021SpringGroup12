package cmpe451.group12.beabee.goalspace.controller;


import cmpe451.group12.beabee.goalspace.model.activitystreams.ActivitySchema;
import cmpe451.group12.beabee.goalspace.service.ActivityStreamService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "Get all activities.")
    @GetMapping("/")
    public List<ActivitySchema> getSchemas() {
        return activityStreamService.getSchemas();
    }
}
