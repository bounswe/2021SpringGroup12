package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.ActivitySchemaRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.ActorSchemaRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.ObjectSchemaRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.OriginSchemaRepository;
import cmpe451.group12.beabee.goalspace.model.activitystreams.ActivitySchema;
import cmpe451.group12.beabee.goalspace.model.activitystreams.ActorSchema;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActivityStreamTest {

    private ActivityStreamService activityStreamService;
    private ObjectSchemaRepository objectSchemaRepository;
    private ActorSchemaRepository actorSchemaRepository;
    private OriginSchemaRepository originSchemaRepository;
    private ActivitySchemaRepository activitySchemaRepository;
    private UserRepository userRepository;

    @Before
    public void setUp() {
        objectSchemaRepository = Mockito.mock(ObjectSchemaRepository.class);
        actorSchemaRepository = Mockito.mock(ActorSchemaRepository.class);
        originSchemaRepository = Mockito.mock(OriginSchemaRepository.class);
        activitySchemaRepository = Mockito.mock(ActivitySchemaRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        activityStreamService = new ActivityStreamService(objectSchemaRepository, actorSchemaRepository, originSchemaRepository, activitySchemaRepository, userRepository);
    }

    @Test
    public void whenGetSchemasOfAUserCalledWithValidId_ItShouldReturnSuccess() {
        /**
         * Scenario: users only get feed about themselves and the suers they follow.
         * User1 follows, user2 and 3. So this endpoint should return schema1,2, and 3.
         * but since it sorts with respect to the time. Order of the result should be schema3,2,1.
         */
        Users user = new Users();
        user.setUser_id(1L);
        user.setUsername("user1");
        Users user2 = new Users();
        user2.setUser_id(2L);
        user2.setUsername("user2");
        Users user3 = new Users();
        user3.setUser_id(3L);
        user3.setUsername("user3");
        Users user4 = new Users();
        user4.setUser_id(4L);
        user4.setUsername("user4");
        user.setFollowing(Stream.of(user2, user3).collect(Collectors.toSet()));

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName("user1");
        ActorSchema actorSchema2 = new ActorSchema();
        actorSchema2.setName("user2");
        ActorSchema actorSchema3 = new ActorSchema();
        actorSchema3.setName("user3");
        ActorSchema actorSchema4 = new ActorSchema();
        actorSchema4.setName("user4");

        ActivitySchema schema1 = new ActivitySchema();
        schema1.setActor(actorSchema);
        schema1.setCreatedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5)));

        ActivitySchema schema2 = new ActivitySchema();
        schema2.setActor(actorSchema2);
        schema2.setCreatedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4)));

        ActivitySchema schema3 = new ActivitySchema();
        schema3.setActor(actorSchema3);
        schema3.setCreatedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)));

        ActivitySchema schema4 = new ActivitySchema();
        schema4.setActor(actorSchema4);
        schema4.setCreatedAt(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(activitySchemaRepository.findAll()).thenReturn(Stream.of(schema1, schema2, schema3, schema4).collect(Collectors.toList()));

        List<ActivitySchema> expected = Stream.of(schema3, schema2, schema1).collect(Collectors.toList());
        List<ActivitySchema> actual = activityStreamService.getSchemasOfAUser(1L);
        Assert.assertEquals(expected, actual);

    }



}
