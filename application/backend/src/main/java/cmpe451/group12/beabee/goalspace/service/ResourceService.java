package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final TaskRepository taskRepository;
    private final QuestionRepository questionRepository;
    private final ReflectionRepository reflectionRepository;
    private final RoutineRepository routineRepository;
    private final EntitiRepository entitiRepository;


    @Transactional
    public ResponseEntity<String> save(MultipartFile resource,Long entiti_id) throws IOException {
        if(resource.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Empty resource!");
        }
        Optional<Entiti> entiti_opt = entitiRepository.findById(entiti_id);
        if (entiti_opt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Entity not found!");
        }

        if(resourceRepository.existsByNameAndEntiti(resource.getOriginalFilename(), entiti_opt.get())){
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
                    .body("A resource with same name already exists under this entity!");
        }

        Resource resourceEntity = new Resource();
        resourceEntity.setName(StringUtils.cleanPath(resource.getOriginalFilename()));
        resourceEntity.setContentType(resource.getContentType());
        resourceEntity.setData(resource.getBytes());
        resourceEntity.setSize(resource.getSize());
        Set<Resource> resourceSet = entiti_opt.get().getResources();

        resourceSet.add(resourceEntity);
        entiti_opt.get().setResources(resourceSet);
        resourceEntity.setEntiti(entiti_opt.get());
        try {
            entitiRepository.save(entiti_opt.get());
            //resourceRepository.save(resourceEntity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Resource uploaded successfully: %s", resource.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Could not upload the resource: %s!", resource.getOriginalFilename()));
        }
    }

    public ResponseEntity<byte[]> getResource(Long id) {

        Optional<Resource> resourceEntityOptional = resourceRepository.findById(id);

        if (!resourceEntityOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found!");
        }
        Resource resourceEntity = resourceEntityOptional.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resourceEntity.getName() + "\"")
                .contentType(MediaType.valueOf(resourceEntity.getContentType()))
                .body(resourceEntity.getData());

    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }

    private Resource mapToFileResponse(Resource resourceEntity) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v2/resources/")
                .path(resourceEntity.getId().toString())
                .toUriString();
        Resource resourceResponse = new Resource();
        resourceResponse.setId(resourceEntity.getId());
        resourceResponse.setName(resourceEntity.getName());
        resourceResponse.setContentType(resourceEntity.getContentType());
        resourceResponse.setSize(resourceEntity.getSize());
        resourceResponse.setUrl(downloadURL);
        resourceResponse.setCreatedAt(resourceEntity.getCreatedAt());

        return resourceResponse;
    }

    public ResponseEntity<String> delete(Long id) {
        Optional<Resource> resourceEntityOptional = resourceRepository.findById(id);

        if(resourceEntityOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resource not found!");
        }
        try {
            resourceRepository.delete(resourceEntityOptional.get());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Resource deleted successfully: %s", resourceEntityOptional.get().getName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Could not delete the resource: %s!", resourceEntityOptional.get().getName()));
        }
    }
}