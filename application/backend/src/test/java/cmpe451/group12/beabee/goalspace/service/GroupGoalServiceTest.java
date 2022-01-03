package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.common.util.UUIDShortener;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalGetDto;
import cmpe451.group12.beabee.goalspace.dto.goals.GroupGoalPostDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.login.mapper.UserCredentialsGetMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

public class GroupGoalServiceTest
{
    UserRepository userRepository;
    GroupGoalService groupGoalService;
    GroupGoalPostMapper groupGoalPostMapper;
    GroupGoalRepository groupGoalRepository;
    ActivityStreamService activityStreamService;
    GroupGoalGetMapper groupGoalGetMapper;
    GroupGoalShortMapper groupGoalShortMapper;
    SubgoalShortMapper subgoalShortMapper;
    SubgoalPostMapper subgoalPostMapper;
    SubgoalRepository subgoalRepository;
    EntitiShortMapper entitiShortMapper;
    UserCredentialsGetMapper userCredentialsGetMapper;
    UUIDShortener uuidShortener;
    SubgoalGetMapper subgoalGetMapper;

    TagRepository tagRepository;


    @Before
    public void setup()
    {
        groupGoalRepository = Mockito.mock(GroupGoalRepository.class);
        subgoalRepository = Mockito.mock(SubgoalRepository.class);
        groupGoalPostMapper = Mockito.mock(GroupGoalPostMapper.class);
        subgoalPostMapper = Mockito.mock(SubgoalPostMapper.class);
        subgoalShortMapper = Mockito.mock(SubgoalShortMapper.class);
        groupGoalGetMapper = Mockito.mock(GroupGoalGetMapper.class);
        groupGoalShortMapper = Mockito.mock(GroupGoalShortMapper.class);
        userRepository = Mockito.mock(UserRepository.class);
        entitiShortMapper = Mockito.mock(EntitiShortMapper.class);
        userCredentialsGetMapper = Mockito.mock(UserCredentialsGetMapper.class);
        activityStreamService = Mockito.mock(ActivityStreamService.class);
        uuidShortener = Mockito.mock(UUIDShortener.class);
        subgoalGetMapper = Mockito.mock(SubgoalGetMapper.class);

        tagRepository = Mockito.mock(TagRepository.class);
        groupGoalService = new GroupGoalService(groupGoalRepository, subgoalRepository, groupGoalPostMapper,
                subgoalPostMapper, subgoalShortMapper, groupGoalGetMapper, groupGoalShortMapper, userRepository, entitiShortMapper,
                userCredentialsGetMapper, activityStreamService, uuidShortener, subgoalGetMapper, tagRepository);
    }

    private Users getRandomUser()
    {
        Random random = new Random();
        return Users.builder()
                .user_id(random.nextLong())
                .email(Long.toString(random.nextLong()))
                .username(Long.toString(random.nextLong()))
                .password(Long.toString(random.nextLong()))
                .password_reset_token(Long.toString(random.nextLong()))
                .password_reset_token_expiration_date(Date.from(Instant.now()))
                .goals(new HashSet<>())
                .groupgoals(new HashSet<>())
                .memberOf(new HashSet<>())
                .assigned(new HashSet<>())
                .entities(new HashSet<>())
                .followers(new HashSet<>())
                .following(new HashSet<>())
                .build();
    }

    private GroupGoal getRandomGroupGoal()
    {
        Users user = getRandomUser();
        Random random = new Random();
        GroupGoal groupGoal = GroupGoal.builder()
                .id(new Random().nextLong())
                .creator(user)
                .members(new HashSet<>(Set.of(user)))
                .token(Long.toString(random.nextLong()))
                .entities(new HashSet<>())
                .subgoals(new HashSet<>())
                .build();
        user.setMemberOf(new HashSet<>(Set.of(groupGoal)));
        return groupGoal;
    }

    @Test
    public void createGoalWithNonexistentUser_returnError()
    {
        Long user_id = 0L;
        Mockito.when(userRepository.findById(user_id)).thenReturn(Optional.empty());
        Assert.assertThrows(ResponseStatusException.class, () ->
        {
            groupGoalService.createAGroupgoal(user_id,
                    GroupGoalPostDTO.builder()
                            .title("test_title")
                            .description("test_description")
                            .build()
            );
        });
    }

    @Test
    public void createGoalWithExistingUser_returnSuccess()
    {
        Users user = Users.builder()
                .user_id(0L)
                .username("username")
                .build();

        GroupGoalPostDTO groupGoalPostDTO = GroupGoalPostDTO.builder()
                .title("test_title")
                .description("test_description")
                .build();

        Mockito.when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));
        Mockito.when(groupGoalPostMapper.mapToEntity(groupGoalPostDTO)).thenReturn(new GroupGoal());

        MessageResponse response = groupGoalService.createAGroupgoal(user.getUser_id(), groupGoalPostDTO);

        Mockito.verify(groupGoalPostMapper, Mockito.times(1)).mapToEntity(groupGoalPostDTO);
        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(activityStreamService, Mockito.times(1)).createGroupGoalSchema(Mockito.eq(user.getUsername()), Mockito.any(GroupGoal.class));

        Assert.assertEquals(new MessageResponse("Group Goal added successfully.", MessageType.SUCCESS), response);
    }

    @Test
    public void getGroupGoalTest_success()
    {
        GroupGoal groupGoal = getRandomGroupGoal();
        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));
        Mockito.when(groupGoalGetMapper.mapToDto(groupGoal)).thenReturn(new GroupGoalGetDto());

        Mockito.when(subgoalShortMapper.mapToDto(new ArrayList<>())).thenReturn(List.of());

        GroupGoalGetDto expected = new GroupGoalGetDto();
        expected.setUser_id(groupGoal.getCreator().getUser_id());
        expected.setEntities(new HashSet<>());
        expected.setSubgoals(new HashSet<>());

        GroupGoalGetDto actual = groupGoalService.getAGroupgoal(groupGoal.getId());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getGroupGoalTest_failure()
    {
        Long goal_id = 0L;
        Mockito.when(groupGoalRepository.findById(goal_id)).thenReturn(Optional.empty());

        Assert.assertThrows(ResponseStatusException.class, () ->
        {
            groupGoalService.getAGroupgoal(goal_id);
        });
    }

    //Checking getGroupgoalsCreatedByAUser is unnecessary since it only calls repositories.

    @Test
    public void getGroupgoalsOfAUserTest_failure()
    {
        Long user_id = 0L;
        Mockito.when(userRepository.findById(user_id)).thenReturn(Optional.empty());

        Assert.assertThrows(ResponseStatusException.class, () ->
        {
            groupGoalService.getGroupgoalsOfAUser(user_id);
        });
    }

    //Checking getGroupgoalsOfAUserTest for success is only testing the repository, so it is unnecessary

    @Test
    public void regenerateTokenTest_failure()
    {
        Long goal_id = 0L;
        Mockito.when(groupGoalRepository.findById(goal_id)).thenReturn(Optional.empty());

        Assert.assertThrows(ResponseStatusException.class, () ->
        {
            groupGoalService.regenerateToken(goal_id);
        });
    }

    @Test
    public void regenerateTokenTest_success()
    {
        String newToken = "NEWTOKEN";
        GroupGoal groupGoal = getRandomGroupGoal();

        Mockito.when(uuidShortener.randomShortUUID()).thenReturn(newToken);

        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));

        groupGoal.setToken(newToken.substring(0, 6));

        MessageResponse actual = groupGoalService.regenerateToken(groupGoal.getId());

        Assert.assertEquals(new MessageResponse("Group goal token regenerated.", MessageType.SUCCESS), actual);
        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(groupGoal);
    }

    @Test
    public void updateAGroupgoalTest_failure()
    {
        GroupGoalGetDto groupGoalGetDto = GroupGoalGetDto.builder()
                .id(0L)
                .build();
        Mockito.when(groupGoalRepository.findById(groupGoalGetDto.getId())).thenReturn(Optional.empty());

        Assert.assertThrows(ResponseStatusException.class, () ->
        {
            groupGoalService.updateAGroupgoal(groupGoalGetDto);
        });
    }

    @Test
    public void updateAGroupgoalTest_success()
    {
        GroupGoal groupGoal = getRandomGroupGoal();

        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));

        GroupGoalGetDto groupGoalGetDto = GroupGoalGetDto.builder()
                .id(groupGoal.getId())
                .title("newTitle")
                .description("newDescription")
                .isDone(true)
                .build();

        MessageResponse actual = groupGoalService.updateAGroupgoal(groupGoalGetDto);

        Assert.assertEquals(new MessageResponse("Group Goal updated!", MessageType.SUCCESS), actual);

        groupGoal.setTitle(groupGoalGetDto.getTitle());
        //groupGoal.setDescription(groupGoalGetDto.getDescription());
        groupGoal.setIsDone(groupGoalGetDto.getIsDone());

        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(groupGoal);
    }

    @Test
    public void deleteGroupgoalTest_failure() {
        Long goal_id = 0L;

        Mockito.when(groupGoalRepository.findById(goal_id)).thenReturn(Optional.empty());

        Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.deleteGroupgoal(goal_id);
        });
    }

    @Test
    public void deleteGroupgoalTest_success() {
        GroupGoal groupGoal = getRandomGroupGoal();
        Users user = groupGoal.getCreator();

        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));
        Mockito.when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));

        MessageResponse actual = groupGoalService.deleteGroupgoal(groupGoal.getId());

        user.setMemberOf(new HashSet<>());

        Assert.assertEquals(new MessageResponse("Group goal deleted.", MessageType.SUCCESS), actual);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(activityStreamService, Mockito.times(1)).deleteGroupGoalSchema(groupGoal.getCreator(), groupGoal);
        Mockito.verify(groupGoalRepository, Mockito.times(1)).deleteById(groupGoal.getId());
    }

    @Test
    public void createSubgoalTest_failure() {
        SubgoalPostDTO subgoalPostDTO = SubgoalPostDTO.builder()
                        .main_groupgoal_id(0L)
                        .build();

        Mockito.when(groupGoalRepository.findById(subgoalPostDTO.getMain_groupgoal_id())).thenReturn(Optional.empty());
        Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.createSubgoal(subgoalPostDTO);
        });
    }

    @Test
    public void createSubgoalTest_success() {
        GroupGoal groupGoal = getRandomGroupGoal();
        SubgoalPostDTO subgoalPostDTO = SubgoalPostDTO.builder()
                .main_groupgoal_id(groupGoal.getId())
                .build();
        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));

        Subgoal subgoal = new Subgoal();

        Mockito.when(subgoalPostMapper.mapToEntity(subgoalPostDTO)).thenReturn(subgoal);

        subgoal.setMainGroupgoal(groupGoal);
        subgoal.setIsDone(Boolean.FALSE);
        subgoal.setCreator(groupGoal.getCreator());
        subgoal.setRating(0D);

        MessageResponse actual = groupGoalService.createSubgoal(subgoalPostDTO);
        Assert.assertEquals(new MessageResponse("Subgoal added.", MessageType.SUCCESS), actual);

        Mockito.verify(subgoalRepository, Mockito.times(1)).save(subgoal);
    }

    @Test
    public void joinWithTokenTest_failure() {
        String token = "token";
        Long user_id = 0L;

        Mockito.when(groupGoalRepository.findByToken(token))
                .thenReturn(Optional.empty()) // groupGoal not found
                .thenReturn(Optional.of(new GroupGoal())); // user not found

        Mockito.when(userRepository.findById(user_id)).thenReturn(Optional.empty());

        // This is for the
        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.joinWithToken(user_id, token);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.joinWithToken(user_id, token);
        });

        Assert.assertEquals("Token is not valid!", exception1.getReason());
        Assert.assertEquals("User does not exist!", exception2.getReason());
    }

    @Test
    public void joinWithTokenTest_success() {
        GroupGoal groupGoal = getRandomGroupGoal();
        groupGoal.setMembers(new HashSet<>());

        Users user = getRandomUser();

        Mockito.when(groupGoalRepository.findByToken(groupGoal.getToken())).thenReturn(Optional.of(groupGoal));
        Mockito.when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));

        groupGoal.getMembers().add(user);

        GroupGoalDTOShort dtoShort = new GroupGoalDTOShort();
        dtoShort.setId(groupGoal.getId());

        Mockito.when(groupGoalShortMapper.mapToDto(groupGoal)).thenReturn(dtoShort);


        Assert.assertEquals(dtoShort, groupGoalService.joinWithToken(user.getUser_id(), groupGoal.getToken()));

        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(groupGoal);
        Mockito.verify(activityStreamService, Mockito.times(1)).joinGroupGoal(user, groupGoal);
    }

    @Test
    public void addMemberTest_failure() {
        Long goal_id = 0L;
        String username = "username";

        Mockito.when(groupGoalRepository.findById(goal_id))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(new GroupGoal()));

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.addMember(goal_id, username);
        });

        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.addMember(goal_id, username);
        });

        Assert.assertEquals("Group goal does not exist", exception1.getReason());
        Assert.assertEquals("User does not exist", exception2.getReason());
    }

    // This method is deprecated
//    @Test
//    public void addMemberTest_success() {
//        GroupGoal groupGoal = getRandomGroupGoal();
//        groupGoal.setMembers(new HashSet<>());
//
//        Users user = getRandomUser();
//
//        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));
//        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
//
//        groupGoal.getMembers().add(user);
//
//        GroupGoalDTOShort dtoShort = new GroupGoalDTOShort();
//        dtoShort.setId(groupGoal.getId());
//
//        Mockito.when(groupGoalShortMapper.mapToDto(groupGoal)).thenReturn(dtoShort);
//
//
//        Assert.assertEquals(dtoShort, groupGoalService.addMember(groupGoal.getId(), user.getUsername()));
//
//        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(groupGoal);
//        Mockito.verify(activityStreamService, Mockito.times(1)).joinGroupGoal(user, groupGoal);
//    }

    @Test
    public void leaveGroupGoalTest_failure() {
        GroupGoal groupGoal = getRandomGroupGoal();
        Users user = groupGoal.getCreator();

        Mockito.when(groupGoalRepository.findById(groupGoal.getId()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(groupGoal));

        Mockito.when(userRepository.findById(user.getUser_id()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(user));

        ResponseStatusException exception1 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.leaveGroupGoal(groupGoal.getId(), user.getUser_id());
        });
        ResponseStatusException exception2 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.leaveGroupGoal(groupGoal.getId(), user.getUser_id());
        });
        ResponseStatusException exception3 = Assert.assertThrows(ResponseStatusException.class, () -> {
            groupGoalService.leaveGroupGoal(groupGoal.getId(), user.getUser_id());
        });

        Assert.assertEquals("Group goal does not exist", exception1.getReason());
        Assert.assertEquals("User does not exist", exception2.getReason());
        Assert.assertEquals("User cannot leave a group goal they created!", exception3.getReason());
    }

    @Test
    public void leaveGroupGoalTest_success() {
        GroupGoal groupGoal = getRandomGroupGoal();

        Users user = getRandomUser();
        groupGoal.getMembers().add(user);

        Mockito.when(groupGoalRepository.findById(groupGoal.getId())).thenReturn(Optional.of(groupGoal));
        Mockito.when(userRepository.findById(user.getUser_id())).thenReturn(Optional.of(user));

        groupGoal.getMembers().remove(user);

        MessageResponse actual = groupGoalService.leaveGroupGoal(groupGoal.getId(), user.getUser_id());

        Assert.assertEquals(new MessageResponse("User left group goal successfully!", MessageType.SUCCESS), actual);

        Mockito.verify(groupGoalRepository, Mockito.times(1)).save(groupGoal);
        Mockito.verify(activityStreamService, Mockito.times(1)).leaveGroupGoal(user, groupGoal);
    }
}
