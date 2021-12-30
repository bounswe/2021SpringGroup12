package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.ActorSchemaRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.CreateSchemaRepository;
import cmpe451.group12.beabee.goalspace.Repository.activitistreams.ObjectSchemaRepository;
import cmpe451.group12.beabee.goalspace.model.activitystreams.ActorSchema;
import cmpe451.group12.beabee.goalspace.model.activitystreams.CreateSchema;
import cmpe451.group12.beabee.goalspace.model.activitystreams.ObjectSchema;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityStreamService {

    private final CreateSchemaRepository createSchemaRepository;
    private final ObjectSchemaRepository objectSchemaRepository;
    private final ActorSchemaRepository actorSchemaRepository;

    public void createGoalSchema(String username, Goal goal){
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goal.getDescription());
        objectSchema.setName(goal.getTitle());
        objectSchema.setType(ObjectSchema.Type.GOAL);
        objectSchema.setLink("/v2/goals/"+goal.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(username);
        actorSchemaRepository.save(actorSchema);

        CreateSchema createSchema = new CreateSchema();
        createSchema.setActor(actorSchema);
        createSchema.setObject(objectSchema);
        createSchema.setSummary(username + " created "+ goal.getTitle() + " goal.");
        createSchemaRepository.save(createSchema);

    }

    public void createGroupGoalSchema(String username, GroupGoal goal){
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goal.getDescription());
        objectSchema.setName(goal.getTitle());
        objectSchema.setType(ObjectSchema.Type.GROUPGOAL);
        objectSchema.setLink("/v2/groupgoals/"+goal.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(username);
        actorSchemaRepository.save(actorSchema);

        CreateSchema createSchema = new CreateSchema();
        createSchema.setActor(actorSchema);
        createSchema.setObject(objectSchema);
        createSchema.setSummary(username + " created "+ goal.getTitle() + " group goal.");
        createSchemaRepository.save(createSchema);

    }


    public CreateSchema getACreateSchema(Long id){
        CreateSchema createSchema = createSchemaRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Create schema not found!"));
        return createSchema;
    }
    public List<CreateSchema> getCreateSchemas(){
        List<CreateSchema> createSchemas = createSchemaRepository.findAll().stream()
                .sorted((i1, i2) -> i2.getCreatedAt().compareTo(i1.getCreatedAt())).
                collect(Collectors.toList());
        return createSchemas;
    }
}
