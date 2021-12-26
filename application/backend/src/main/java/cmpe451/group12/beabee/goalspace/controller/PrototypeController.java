package cmpe451.group12.beabee.goalspace.controller;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.service.PrototypeService;
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
@RequestMapping(value = "/v2/prototypes")
public class PrototypeController {
    private final PrototypeService prototypeService;
    @ApiOperation(value = "Get all goal prototypes.")
    @GetMapping("/")
    public List<GoalPrototypeDTO> getPrototypes() {
        return prototypeService.getPrototypes();
    }

    @ApiOperation(value = "Get a goal prototype with the given id.")
    @GetMapping("/{id}")
    public GoalPrototypeDTO getAPrototype(@PathVariable @ApiParam(value = "Id of the goal prototype.", example = "5") Long id) {
        return prototypeService.getAPrototype(id);
    }
    @ApiOperation(value = "Publish a goal with the given id in the marketplace.")
    @PostMapping("/publish/{id}")public MessageResponse publishAGoal(@PathVariable @ApiParam(value = "Id of the goal.", example = "5") Long id) {
        return prototypeService.publishAGoal(id);
    }

    @ApiOperation(value = "Get an entiti prototypes with the given id.")
    @GetMapping("/entiti/{id}")
    public EntitiPrototypeDTO getAnEntitiPrototype(@PathVariable @ApiParam(value = "Id of the entity prototype.", example = "5") Long id) {
        return prototypeService.getAnEntitiPrototype(id);
    }
    @ApiOperation(value = "Get a goal prototypes with the given id.")
    @GetMapping("/subgoal/{id}")
    public SubgoalPrototypeDTO getASubgoalPrototype(@PathVariable @ApiParam(value = "Id of the subgoal prototype.", example = "5") Long id) {
        return prototypeService.getASubgoalPrototype(id);
    }
}
