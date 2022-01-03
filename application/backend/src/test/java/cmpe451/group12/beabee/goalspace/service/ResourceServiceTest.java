package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.resources.ResourceRepository;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ResourceServiceTest {

    private ResourceRepository resourceRepository;

    private EntitiRepository entitiRepository;
    private ResourceService resourceService;
    private ActivityStreamService activityStreamService;

    @Before
    public void setUp() {
        resourceRepository = Mockito.mock(ResourceRepository.class);
        entitiRepository = Mockito.mock(EntitiRepository.class);
        activityStreamService = Mockito.mock(ActivityStreamService.class);
        resourceService = new ResourceService(resourceRepository, entitiRepository, activityStreamService);
    }

    @Test
    public void whenGetResourceCalledWithValidId_ItShouldReturnSuccess() {
        Resource resource = new Resource();
        resource.setId(1L);
        resource.setContentType("application/pdf");
        resource.setData(new byte[5]);

        Mockito.when(resourceRepository.findById(resource.getId())).thenReturn(Optional.of(resource));

        ResponseEntity<byte[]> result = resourceService.getResource(resource.getId());
        Assert.assertEquals(resource.getData(), result.getBody());
        Assert.assertEquals(resource.getContentType(), result.getHeaders().getContentType().getType() + "/" + result.getHeaders().getContentType().getSubtype());

        Mockito.verify(resourceRepository).findById(resource.getId());
    }

    @Test
    public void whenSaveCalledWithExistingName_ItShouldReturnError() throws IOException {
        Long entiti_id = 5L;
        MultipartFile resource = new MockMultipartFile("fileThatDoesNotExists.txt",
                "fileThatDoesNotExists.txt",
                "text/plain",
                "This is a dummy file content".getBytes(StandardCharsets.UTF_8));

        Entiti entiti = new Task();
        entiti.setId(entiti_id);

        Mockito.when(entitiRepository.findById(entiti_id)).thenReturn(Optional.of(entiti));
        Mockito.when(resourceRepository.existsByNameAndEntiti(resource.getOriginalFilename(), entiti)).thenReturn(Boolean.TRUE);

        ResponseEntity<String> result = resourceService.save(resource, 5L);
        Assert.assertEquals("A resource with same name already exists under this entity!", result.getBody());
        Assert.assertEquals(451, result.getStatusCodeValue());

        Mockito.verify(entitiRepository).findById(entiti_id);
        Mockito.verify(resourceRepository).existsByNameAndEntiti(resource.getOriginalFilename(), entiti);

    }
}