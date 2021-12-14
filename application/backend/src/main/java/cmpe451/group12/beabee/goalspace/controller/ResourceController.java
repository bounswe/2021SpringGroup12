package cmpe451.group12.beabee.goalspace.controller;

import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import cmpe451.group12.beabee.goalspace.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8085")
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/v2/resources")
public class ResourceController {

private final ResourceService resourceService;

    @PostMapping(value = "/{entiti_id}")
    public ResponseEntity<String> upload(@RequestParam("resource") MultipartFile resource,@PathVariable("entiti_id") Long entiti_id) throws IOException {
        return resourceService.save(resource,entiti_id);
    }

    @GetMapping(value = "/")
    public List<Resource> list() {
        return resourceService.getAllResources();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") Long id) {
        return resourceService.getResource(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return resourceService.delete(id);
    }
}
