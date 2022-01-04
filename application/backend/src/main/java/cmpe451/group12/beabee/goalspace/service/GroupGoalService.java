package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.common.util.UUIDShortener;
import cmpe451.group12.beabee.goalspace.Repository.goals.GroupGoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.*;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.entities.Question;
import cmpe451.group12.beabee.goalspace.model.entities.Reflection;
import cmpe451.group12.beabee.goalspace.model.entities.Routine;
import cmpe451.group12.beabee.goalspace.model.entities.Task;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.login.dto.UserCredentialsGetDTO;
import cmpe451.group12.beabee.login.mapper.UserCredentialsGetMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GroupGoalService
{
    private final GroupGoalRepository groupGoalRepository;
    private final SubgoalRepository subgoalRepository;
    private final GroupGoalPostMapper groupGoalPostMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final GroupGoalGetMapper groupGoalGetMapper;
    private final GroupGoalShortMapper groupGoalShortMapper;
    private final UserRepository userRepository;
    private final EntitiShortMapper entitiShortMapper;
    private final UserCredentialsGetMapper userCredentialsGetMapper;
    private final ActivityStreamService activityStreamService;
    private final UUIDShortener uuidShortener;
    private final SubgoalGetMapper subgoalGetMapper;

    private final TagRepository tagRepository;


    private Set<EntitiDTOShort> extractEntities(GroupGoal groupGoal){

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                groupGoal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    public GroupGoalGetDto getAGroupgoal(Long groupgoal_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );
        GroupGoalGetDto groupGoalGetDto = groupGoalGetMapper.mapToDto(groupGoal);
        groupGoalGetDto.setUser_id(groupGoal.getCreator().getUser_id());
        groupGoalGetDto.setSubgoals(new HashSet<>(subgoalShortMapper.mapToDto(new ArrayList<>(groupGoal.getSubgoals()))));
        groupGoalGetDto.setEntities(extractEntities(groupGoal));
        groupGoalGetDto.setTags(groupGoal.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        //groupGoalGetDto.setMembers(userCredentialsGetMapper.mapToDto(groupGoal.getMembers().stream().map(x->{x.setPassword("***"); return x;}).collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        return groupGoalGetDto;
    }

    public List<GroupGoalDTOShort> getGroupgoalsCreatedByAUser(Long user_id) {
        return groupGoalShortMapper.mapToDto(groupGoalRepository.findAllByUserId(user_id));
    }

    public List<GroupGoalDTOShort> getGroupgoalsOfAUser(Long user_id) {
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        return groupGoalShortMapper.mapToDto(new ArrayList<>(user.getMemberOf()));
    }

    public MessageResponse regenerateToken(Long groupgoal_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );

        groupGoal.setToken(uuidShortener.randomShortUUID().substring(0,6));
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("Group goal token regenerated.", MessageType.SUCCESS);
    }

    public MessageResponse updateAGroupgoal(GroupGoalGetDto groupGoalGetDto) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupGoalGetDto.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!")
        );

        if (groupGoalGetDto.getTitle() != null){
            groupGoal.setTitle(groupGoalGetDto.getTitle());
        }
        if (groupGoalGetDto.getDescription() != null){
            groupGoal.setDescription(groupGoalGetDto.getDescription());
        }
        if (groupGoalGetDto.getIsDone() != null){
            groupGoal.setIsDone(groupGoalGetDto.getIsDone());
        }
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("Group Goal updated!", MessageType.SUCCESS);
    }


    public MessageResponse createAGroupgoal(Long user_id, GroupGoalPostDTO groupGoalPostDTO)
    {
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
        );
        GroupGoal new_groupgoal = groupGoalPostMapper.mapToEntity(groupGoalPostDTO);
        new_groupgoal.setIsDone(Boolean.FALSE);
        new_groupgoal.setCreator(user);

        //Do we still need this? -nah. doesn't matter whether we keep it
        new_groupgoal.setGoalType(GoalType.GROUPGOAL);

        //Use UUID converted to URL62 Base to guarantee uniqueness and improve readability
        new_groupgoal.setToken(uuidShortener.randomShortUUID().substring(0,6));

        //Add creator to the member list
        HashSet<Users> members = new HashSet<>();
        members.add(user);
        new_groupgoal.setMembers(members);
        groupGoalRepository.save(new_groupgoal);
        activityStreamService.createGroupGoalSchema(user.getUsername(),new_groupgoal);
        return new MessageResponse("Group Goal added successfully.", MessageType.SUCCESS);
    }

    public MessageResponse deleteGroupgoal(Long groupgoal_id)
    {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!"));
        for(Users user : groupGoal.getMembers()) {
            Optional<Users> userOptional = userRepository.findById(user.getUser_id());
            if(userOptional.isPresent()) {
                user.getMemberOf().remove(groupGoal);
            }
            userRepository.save(user);
        }
        activityStreamService.deleteGroupGoalSchema(groupGoal.getCreator(),groupGoal);
        groupGoalRepository.deleteById(groupGoal.getId());
        return new MessageResponse("Group goal deleted.", MessageType.SUCCESS);
    }

    public MessageResponse createSubgoal(SubgoalPostDTO subgoal_dto)
    {
        GroupGoal groupGoal = groupGoalRepository.findById(subgoal_dto.getMain_groupgoal_id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal not found!"));
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoal_dto);
        new_subgoal.setMainGroupgoal(groupGoal);
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setCreator(groupGoal.getCreator());
        new_subgoal.setRating(0D);
        subgoalRepository.save(new_subgoal);

        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    public GroupGoalDTOShort joinWithToken(Long user_id, String token)
    {
        GroupGoal groupGoal = groupGoalRepository.findByToken(token).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token is not valid!")
        );

        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist!")
        );

        groupGoal.getMembers().add(user);

        groupGoalRepository.save(groupGoal);
        activityStreamService.joinGroupGoal(user,groupGoal);
        return groupGoalShortMapper.mapToDto(groupGoal);
    }

    public MessageResponse addMember(Long groupgoal_id, String username) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal does not exist")
        );
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );

        groupGoal.getMembers().add(user);
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("User added to group successfully!", MessageType.SUCCESS);
    }

    public MessageResponse leaveGroupGoal(Long groupgoal_id, Long user_id) {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group goal does not exist")
        );
        Users user = userRepository.findById(user_id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist")
        );
        if(Objects.equals(groupGoal.getCreator().getUser_id(), user.getUser_id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot leave a group goal they created!");
        }
        groupGoal.getMembers().remove(user);
        groupGoalRepository.save(groupGoal);
        activityStreamService.leaveGroupGoal(user,groupGoal);
        return new MessageResponse("User left group goal successfully!", MessageType.SUCCESS);
    }

    private static Stream<Subgoal> flatMapRecursive(Subgoal item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(GroupGoalService::flatMapRecursive));
    }

    public List<SubgoalGetDTO> getSubgoalsOfGroupGoal(Long groupgoal_id)
    {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupgoal not found!");
        });

        List<Subgoal> all_subgoals = groupGoal.getSubgoals().stream()
                .flatMap(GroupGoalService::flatMapRecursive).collect(Collectors.toList());
        return subgoalGetMapper.mapToDto(all_subgoals);
    }

    // ################# TAGS #####################
    protected Optional<Tag> getTagByName(String name) throws IOException, ParseException
    {
        List<Tag> tag_from_db = tagRepository.findByName(name);
        if (tag_from_db.size()>0) {
            return Optional.of(tag_from_db.get(0));
        }
        URL url = new URL("https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" + name + "&format=json&errorformat=plaintext&language=en&uselang=en&type=item");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status != 200) {
            return Optional.ofNullable(null);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        JSONParser parser = new JSONParser();
        JSONObject resp = (JSONObject) parser.parse(content.toString());
        JSONArray search = (JSONArray) resp.get("search");
        if(1 > search.size()){
            return Optional.ofNullable(null);
        }
        JSONObject first_res = (JSONObject) search.get(0);
        String id = first_res.get("id").toString();
        con.disconnect();

        if (StringUtils.countMatches(name, " ") > 2) {
            return Optional.ofNullable(null);
        }
        Tag new_tag = new Tag();
        new_tag.setGoals(new HashSet<>());
        new_tag.setGroup_goals(new HashSet<>());
        new_tag.setId(id);
        new_tag.setName(name);
        tagRepository.save(new_tag);
        return Optional.of(new_tag);
    }

    protected Optional<Tag> getTagById(String id) throws IOException {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            return tagOptional;
        }
        URL url = new URL("https://www.wikidata.org/wiki/Special:EntityData/" + id + ".json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status != 200) {
            return Optional.ofNullable(null);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        JSONParser parser = new JSONParser();
        try {
            JSONObject object = (JSONObject) ((JSONObject) ((JSONObject) parser.parse(content.toString())).get("entities")).get(id);
            String name = (String) ((JSONObject) ((JSONObject) object.get("labels")).get("en")).get("value");

            if (StringUtils.countMatches(name, " ") > 2) {
                return Optional.ofNullable(null);
            }
            Tag new_tag = new Tag();
            new_tag.setName(name);
            new_tag.setId(id);
            new_tag.setGoals(new HashSet<>());
            tagRepository.save(new_tag);
            return Optional.of(new_tag);
        } catch (Exception e) {
            return Optional.ofNullable(null);
        }
    }
    public MessageResponse removeTag(Long groupgoal_id, String tag) throws IOException, ParseException {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupgoal not found!"));
        Set<Tag> tags = groupGoal.getTags();
        if(!groupGoal.getTags().stream().map(x->x.getName()).collect(Collectors.toSet()).contains(tag)){
            return new MessageResponse("Groupgoal does not contain that tag!",MessageType.ERROR);
        }
        List<Tag> tags_from_db = tagRepository.findByName(tag);

        Set<String> related_ids = findRelatedTagIds(Stream.of(tag).collect(Collectors.toSet()));
        groupGoal.getTags().removeAll(tags_from_db);
        Set<Tag> related_tags = new HashSet<>();
        for (String id : related_ids) {
            if (id == null) continue;
            Optional<Tag> tag_x = getTagById(id);
            if (tag_x.isPresent() && !tags.contains(tag_x.get().getName())) {
                related_tags.add(tag_x.get());
            }
        }
        groupGoal.getHiddentags().removeAll(related_tags);
        groupGoalRepository.save(groupGoal);
        return new MessageResponse("Tags removed successfully!", MessageType.SUCCESS);
    }
    public MessageResponse addTags(Long groupgoal_id, Set<String> tags) throws IOException, ParseException {
        GroupGoal groupGoal = groupGoalRepository.findById(groupgoal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupgoal not found!"));
        Set<Tag> topic_ids = new HashSet<>();
        for (String tag : tags) {
            Optional<Tag> tagEntiti = getTagByName(tag);
            if (tagEntiti.isEmpty()) {
                continue;
            }
            tagEntiti.get().getGroup_goals().add(groupGoal);
            topic_ids.add(tagEntiti.get());
        }
        Set<String> related_ids = findRelatedTagIds(tags);
        Set<Tag> related_tags = new HashSet<>();
        for (String id : related_ids) {
            if (id == null) continue;
            Optional<Tag> tag_x = getTagById(id);
            if (tag_x.isPresent() && !tags.contains(tag_x.get().getName())) {
                related_tags.add(tag_x.get());
            }
        }
        tagRepository.saveAll(related_tags);
        groupGoal.getHiddentags().addAll(related_tags);
        tagRepository.saveAll(topic_ids);
        groupGoal.getTags().addAll(topic_ids);
        groupGoalRepository.save(groupGoal);

        return new MessageResponse("Tags added successfully.", MessageType.SUCCESS);
    }

    protected Set<String> findRelatedTagIds(Set<String> tags) throws IOException, ParseException {
        Set<String> related_ids = new HashSet<>();
        for (String name : tags) {
            Optional<Tag> tag_entiti = getTagByName(name);
            if (tag_entiti.isEmpty()) {
                continue;
            }
            related_ids.add(tag_entiti.get().getId());
            URL url = new URL("https://www.wikidata.org/wiki/Special:EntityData/" + tag_entiti.get().getId() + ".json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status != 200) {
                continue;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) ((JSONObject) ((JSONObject) parser.parse(content.toString())).get("entities")).get(tag_entiti.get().getId());
            JSONObject claims = (JSONObject) object.get("claims");

            for (Object key : claims.keySet()) {
                JSONObject value_of_claim = (JSONObject) ((JSONArray) claims.get(key)).get(0);
                try {
                    String new_id = (String) ((JSONObject) ((JSONObject) ((JSONObject) value_of_claim.get("mainsnak")).get("datavalue")).get("value")).get("id");
                    related_ids.add(new_id);
                } catch (Exception exception) {
                    continue;
                }
            }

        }
        return related_ids;
    }
}
