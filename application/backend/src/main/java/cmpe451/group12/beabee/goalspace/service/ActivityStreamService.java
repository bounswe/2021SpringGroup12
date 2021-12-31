package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;

import cmpe451.group12.beabee.goalspace.Repository.activitistreams.*;
import cmpe451.group12.beabee.goalspace.enums.ActivityType;
import cmpe451.group12.beabee.goalspace.model.activitystreams.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityStreamService {

    //private final CreateSchemaRepository createSchemaRepository;
    private final ObjectSchemaRepository objectSchemaRepository;
    private final ActorSchemaRepository actorSchemaRepository;
    //private final DeleteSchemaRepository deleteSchemaRepository;
    private final OriginSchemaRepository originSchemaRepository;
    private final ActivitySchemaRepository activitySchemaRepository;

    // For filtering Activity Schemas by User Id
    private final UserRepository userRepository;

    protected void createGoalSchema(Users user, Goal goal) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goal.getDescription());
        objectSchema.setName(goal.getTitle());
        objectSchema.setType(ObjectSchema.Type.GOAL);
        objectSchema.setUrl("/v2/goals/" + goal.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema createGoalSchema = new ActivitySchema();
        createGoalSchema.setType(ActivityType.CREATE);
        createGoalSchema.setActor(actorSchema);
        createGoalSchema.setObjectschema(objectSchema);
        createGoalSchema.setSummary(user.getUsername() + " created " + goal.getTitle() + " goal.");
        activitySchemaRepository.save(createGoalSchema);
    }

    protected void completeGoalSchema(Users user, Goal goal) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goal.getDescription());
        objectSchema.setName(goal.getTitle());
        objectSchema.setType(ObjectSchema.Type.GOAL);
        objectSchema.setUrl("/v2/goals/" + goal.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema createGoalSchema = new ActivitySchema();
        createGoalSchema.setType(ActivityType.UPDATE);
        createGoalSchema.setActor(actorSchema);
        createGoalSchema.setObjectschema(objectSchema);
        createGoalSchema.setSummary(user.getUsername() + " completed " + goal.getTitle() + " goal.");
        activitySchemaRepository.save(createGoalSchema);
    }

    public void createGroupGoalSchema(String username, GroupGoal goal) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goal.getDescription());
        objectSchema.setName(goal.getTitle());
        objectSchema.setType(ObjectSchema.Type.GROUPGOAL);
        objectSchema.setUrl("/v2/groupgoals/" + goal.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(username);
        actorSchemaRepository.save(actorSchema);

        ActivitySchema createGoalSchema = new ActivitySchema();
        createGoalSchema.setType(ActivityType.CREATE);
        createGoalSchema.setActor(actorSchema);
        createGoalSchema.setObjectschema(objectSchema);
        createGoalSchema.setSummary(username + " created " + goal.getTitle() + " group goal.");
        activitySchemaRepository.save(createGoalSchema);

    }

    protected void followUserSchema(Users user, Users target) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setName(target.getUsername());
        objectSchema.setType(ObjectSchema.Type.PERSON);
        objectSchema.setUrl("/v2/users/get/" + target.getUser_id());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema follow_schema = new ActivitySchema();
        follow_schema.setType(ActivityType.FOLLOW);
        follow_schema.setActor(actorSchema);
        follow_schema.setObjectschema(objectSchema);
        follow_schema.setSummary(user.getUsername() + " followed " + target.getUsername() + ".");
        activitySchemaRepository.save(follow_schema);
    }

    protected void publishGoalSchema(Users user, GoalPrototype goalPrototype) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goalPrototype.getDescription());
        objectSchema.setName(goalPrototype.getTitle());
        objectSchema.setType(ObjectSchema.Type.PUBLISH);
        objectSchema.setUrl("/v2/prototypes/" + goalPrototype.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goalPrototype.getTitle());
        originSchema.setType(ObjectSchema.Type.GOAL);
        originSchemaRepository.save(originSchema);

        ActivitySchema publish_goal_schema = new ActivitySchema();
        publish_goal_schema.setType(ActivityType.ADD);
        publish_goal_schema.setActor(actorSchema);
        publish_goal_schema.setObjectschema(objectSchema);
        publish_goal_schema.setOrigin(originSchema);
        publish_goal_schema.setSummary(user.getUsername() + " published " + goalPrototype.getTitle() + " goal.");
        activitySchemaRepository.save(publish_goal_schema);
    }

    protected void republishGoalSchema(Users user, GoalPrototype goalPrototype) {
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goalPrototype.getDescription());
        objectSchema.setName(goalPrototype.getTitle());
        objectSchema.setType(ObjectSchema.Type.UPDATE);
        objectSchema.setUrl("/v2/prototypes/" + goalPrototype.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema republish_schema = new ActivitySchema();
        republish_schema.setType(ActivityType.UPDATE);
        republish_schema.setActor(actorSchema);
        republish_schema.setObjectschema(objectSchema);
        republish_schema.setSummary(user.getUsername() + " published " + goalPrototype.getTitle() + " goal.");
        activitySchemaRepository.save(republish_schema);
    }

    protected void deleteGoalSchema(Users user, Goal goal) {
        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goal.getTitle());
        originSchema.setType(ObjectSchema.Type.GOAL);
        originSchemaRepository.save(originSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema deleteSchema = new ActivitySchema();
        deleteSchema.setType(ActivityType.DELETE);
        deleteSchema.setActor(actorSchema);
        deleteSchema.setObject("/v2/goals/" + goal.getId());// will give 404 :/
        deleteSchema.setOrigin(originSchema);
        deleteSchema.setSummary(user.getUsername() + " deleted " + goal.getTitle() + " goal.");
        activitySchemaRepository.save(deleteSchema);
    }

    protected void deleteGroupGoalSchema(Users user, GroupGoal goal) {
        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goal.getTitle());
        originSchema.setType(ObjectSchema.Type.GROUPGOAL);
        originSchemaRepository.save(originSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema deleteSchema = new ActivitySchema();
        deleteSchema.setType(ActivityType.DELETE);
        deleteSchema.setActor(actorSchema);
        deleteSchema.setObject("/v2/groupgoals/" + goal.getId());
        deleteSchema.setOrigin(originSchema);
        deleteSchema.setSummary(user.getUsername() + " deleted " + goal.getTitle() + " group goal.");
        activitySchemaRepository.save(deleteSchema);
    }

    protected void leaveGroupGoal(Users user, GroupGoal goal){
        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goal.getTitle());
        originSchema.setType(ObjectSchema.Type.GROUPGOAL);
        originSchemaRepository.save(originSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema deleteSchema = new ActivitySchema();
        deleteSchema.setType(ActivityType.LEAVE);
        deleteSchema.setActor(actorSchema);
        deleteSchema.setObject("/v2/groupgoals/" + goal.getId());
        deleteSchema.setOrigin(originSchema);
        deleteSchema.setSummary(user.getUsername() + " leaved from " + goal.getTitle() + " group goal.");
        activitySchemaRepository.save(deleteSchema);
    }

    protected void joinGroupGoal(Users user, GroupGoal goal){
        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goal.getTitle());
        originSchema.setType(ObjectSchema.Type.GROUPGOAL);
        originSchemaRepository.save(originSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        ActivitySchema deleteSchema = new ActivitySchema();
        deleteSchema.setType(ActivityType.JOIN);
        deleteSchema.setActor(actorSchema);
        deleteSchema.setObject("/v2/groupgoals/" + goal.getId());
        deleteSchema.setOrigin(originSchema);
        deleteSchema.setSummary(user.getUsername() + " joine to " + goal.getTitle() + " group goal.");
        activitySchemaRepository.save(deleteSchema);
    }

    protected void copyAGoal(Users user, GoalPrototype goalPrototype){
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(goalPrototype.getDescription());
        objectSchema.setName(goalPrototype.getTitle());
        objectSchema.setType(ObjectSchema.Type.COPY);
        objectSchema.setUrl("/v2/prototypes/" + goalPrototype.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goalPrototype.getTitle());
        originSchema.setType(ObjectSchema.Type.GOAL);
        originSchemaRepository.save(originSchema);

        ActivitySchema publish_goal_schema = new ActivitySchema();
        publish_goal_schema.setType(ActivityType.ADD);
        publish_goal_schema.setActor(actorSchema);
        publish_goal_schema.setObjectschema(objectSchema);
        publish_goal_schema.setOrigin(originSchema);
        publish_goal_schema.setSummary(user.getUsername() + " copied " + goalPrototype.getTitle() + " goal.");
        activitySchemaRepository.save(publish_goal_schema);
    }

    protected void addResource(Users user, Goal goal, Resource resource){
        ObjectSchema objectSchema = new ObjectSchema();
        objectSchema.setContent(resource.getContentType());
        objectSchema.setName(resource.getName());
        objectSchema.setType(ObjectSchema.Type.ADD);
        objectSchema.setUrl("/v2/resources/" + resource.getId());
        objectSchemaRepository.save(objectSchema);

        ActorSchema actorSchema = new ActorSchema();
        actorSchema.setName(user.getUsername());
        actorSchemaRepository.save(actorSchema);

        OriginSchema originSchema = new OriginSchema();
        originSchema.setName(goal.getTitle());
        originSchema.setType(ObjectSchema.Type.GOAL);
        originSchemaRepository.save(originSchema);

        ObjectSchema target_schema = new ObjectSchema();
        target_schema.setContent(goal.getDescription());
        target_schema.setName(goal.getTitle());
        target_schema.setType(ObjectSchema.Type.ADD);
        target_schema.setUrl("/v2/goals/" + goal.getId());
        objectSchemaRepository.save(target_schema);

        ActivitySchema publish_goal_schema = new ActivitySchema();
        publish_goal_schema.setTarget(target_schema);
        publish_goal_schema.setType(ActivityType.ADD);
        publish_goal_schema.setActor(actorSchema);
        publish_goal_schema.setObjectschema(objectSchema);
        publish_goal_schema.setOrigin(originSchema);
        publish_goal_schema.setSummary(user.getUsername() + " added " + resource.getName() + " resource to the goal "+goal.getTitle()+".");
        activitySchemaRepository.save(publish_goal_schema);
    }


    public ActivitySchema getACreateSchema(Long id) {
        return activitySchemaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Create schema not found!"));
    }

    public List<ActivitySchema> getSchemasOfAUser(Long userId) {
        String username = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new)
                .getUsername();

        return activitySchemaRepository.findAll().stream()
                .filter(activitySchema -> activitySchema.getActor().getName().equals(username))
                .sorted((i1, i2) -> i2.getCreatedAt().compareTo(i1.getCreatedAt()))
                .collect(Collectors.toList());

    }
}
