package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.dto.analytics.GoalAnalyticsDTO;
import cmpe451.group12.beabee.goalspace.dto.entities.EntitiDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.*;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.model.entities.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
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
public class GoalService {

    private final GoalRepository goalRepository;
    private final SubgoalRepository subgoalRepository;
    private final GoalPostMapper goalPostMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final GoalGetMapper goalGetMapper;
    private final GoalShortMapper goalShortMapper;
    private final UserRepository userRepository;
    private final EntitiShortMapper entitiShortMapper;
    private final EntitiRepository entitiRepository;
    private final RoutineRepository routineRepository;
    private final ReflectionRepository reflectionRepository;
    private final TaskRepository taskRepository;
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;


    private Set<EntitiDTOShort> extractEntities(Goal goal) {

        Set<EntitiDTOShort> sublinks = new HashSet<>();

        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Question"))
                        .map(x -> entitiShortMapper.mapToDto((Question) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Task"))
                        .map(x -> entitiShortMapper.mapToDto((Task) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Routine"))
                        .map(x -> entitiShortMapper.mapToDto((Routine) x)).collect(Collectors.toSet()));
        sublinks.addAll(
                goal.getEntities().stream().filter(x -> x.getClass().getSimpleName().equals("Reflection"))
                        .map(x -> entitiShortMapper.mapToDto((Reflection) x)).collect(Collectors.toSet()));
        return sublinks;
    }

    /********************** SEARCH BEGINS *********************************/
    public List<GoalDTOShort> searchGoalUsingTitleAndDescription(String query) {
        List<Goal> all_goals = goalRepository.findAllByDescriptionContainsOrTitleContains(query, query);
        List<GoalDTOShort> goalDTOShorts = goalShortMapper.mapToDto(all_goals);
        return goalDTOShorts;
    }

    //private Set<Tag> searchTagsById(String id){

    //}
    private Set<String> findRelatedTagIds(Set<String> tags) throws IOException, ParseException {
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

    public List<GoalDTOShort> searchGoalUsingTags(String tag) throws IOException, ParseException {
        Set<String> related_ids = findRelatedTagIds(Stream.of(tag).collect(Collectors.toSet()));
        Set<Tag> related_tags = new HashSet<>();
        for (String id : related_ids) {
            if (id == null) continue;
            Optional<Tag> tag_x = getTagById(id);
            if (tag_x.isPresent()) {
                related_tags.add(tag_x.get());
            }
        }

        System.out.println(related_tags.stream().map(x -> x.getName()).collect(Collectors.toList()));
        List<Goal> all_goals = new ArrayList<>();
        related_tags.stream().forEach(x -> {
            all_goals.addAll(goalRepository.findAllByTagsIsContaining(x));
        });
        List<GoalDTOShort> goalDTOShorts = goalShortMapper.mapToDto(all_goals);
        return goalDTOShorts;
    }

    /********************** SEARCH ENDS *********************************/


    public GoalGetDTO getAGoal(Long goal_id) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goal_id);
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        GoalGetDTO goalGetDTO = goalGetMapper.mapToDto(goal_from_db_opt.get());
        goalGetDTO.setUser_id(goal_from_db_opt.get().getCreator().getUser_id());
        goalGetDTO.setSubgoals(subgoalShortMapper.mapToDto(goal_from_db_opt.get().getSubgoals().stream().collect(Collectors.toList())).stream().collect(Collectors.toSet()));
        goalGetDTO.setEntities(extractEntities(goal_from_db_opt.get()));
        return goalGetDTO;
    }

    public List<GoalDTOShort> getGoalsOfAUser(Long user_id) {
        return goalShortMapper.mapToDto(goalRepository.findAllByUserId(user_id));
    }

    public MessageResponse updateAGoal(GoalGetDTO goalGetDTO) {
        Optional<Goal> goal_from_db_opt = goalRepository.findById(goalGetDTO.getId());
        if (goal_from_db_opt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        Goal goal_from_db = goal_from_db_opt.get();
        if (goalGetDTO.getTitle() != null) {
            goal_from_db.setTitle(goalGetDTO.getTitle());
        }
        if (goalGetDTO.getDescription() != null) {
            goal_from_db.setDescription(goalGetDTO.getDescription());
        }
        if (goalGetDTO.getIsDone() != null) {
            goal_from_db.setIsDone(goalGetDTO.getIsDone());
        }
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal updated!", MessageType.SUCCESS);
    }

    /***********TAGS BEGIN**********/
    private Optional<Tag> getTagByName(String name) throws IOException, ParseException {
        Optional<Tag> tag_from_db = tagRepository.findByName(name);
        if (tag_from_db.isPresent()) {
            return tag_from_db;
        }
        URL url = new URL("https://www.wikidata.org/w/api.php?action=wbsearchentities&search=" + name + "&format=json&errorformat=plaintext&language=en&uselang=en&type=item");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        if (status != 200) {
            return Optional.of(null);
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
        JSONObject first_res = (JSONObject) search.get(0);
        String id = first_res.get("id").toString();
        con.disconnect();

        if (StringUtils.countMatches(name, " ") > 2) {
            return Optional.ofNullable(null);
        }
        Tag new_tag = new Tag();
        new_tag.setGoals(new HashSet<>());
        new_tag.setId(id);
        new_tag.setName(name);
        tagRepository.save(new_tag);
        return Optional.of(new_tag);
    }

    private Optional<Tag> getTagById(String id) throws IOException {
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

    public MessageResponse addTags(Long goal_id, Set<String> tags) throws IOException, ParseException {
        Goal goal = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        Set<Tag> topic_ids = new HashSet<>();
        for (String tag : tags) {
            Optional<Tag> tagEntiti = getTagByName(tag);
            if (tagEntiti.isEmpty()) {
                continue;
            }
            tagEntiti.get().getGoals().add(goal);
            topic_ids.add(tagEntiti.get());
        }
        tagRepository.saveAll(topic_ids);
        goal.getTags().addAll(topic_ids);
        goalRepository.save(goal);

        return new MessageResponse("Tags added successfully.", MessageType.SUCCESS);
    }

    /***********TAGS END**********/


    public MessageResponse createAGoal(Long user_id, GoalPostDTO goalPostDTO) {
        Optional<Users> user = userRepository.findById(user_id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        Goal new_goal = goalPostMapper.mapToEntity(goalPostDTO);
        new_goal.setIsDone(Boolean.FALSE);
        new_goal.setCreator(user.get());
        new_goal.setExtension_count(0L);
        new_goal.setGoalType(GoalType.GOAL);
        new_goal.setRating(0D);
//        getTopicIds(new_goal.getTags());
        goalRepository.save(new_goal);
        return new MessageResponse("Goal added successfully.", MessageType.SUCCESS);
    }

    /**
     * When a goal is deleted all of its entities are also deleted.
     *
     * @param goal_id
     * @return
     */
    public MessageResponse deleteGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        List<Entiti> entitiesByGoal = entitiRepository.findAllByGoal(goal_from_db);
        for (Entiti e : entitiesByGoal) {
            if (e.getEntitiType().equals(EntitiType.ROUTINE)) {
                Routine byId = routineRepository.getById(e.getId());
                byId.setDeadline(null);
                byId.setRating(null);
                routineRepository.save(byId);
                routineRepository.deleteById(e.getId());
            }
            if (e.getEntitiType().equals(EntitiType.TASK))
                taskRepository.deleteById(e.getId());
            if (e.getEntitiType().equals(EntitiType.REFLECTION))
                reflectionRepository.deleteById(e.getId());
            if (e.getEntitiType().equals(EntitiType.QUESTION))
                questionRepository.deleteById(e.getId());
        }
        List<Subgoal> all_subgoals = goal_from_db.getSubgoals().stream()
                .flatMap(GoalService::flatMapRecursive).collect(Collectors.toList());
        all_subgoals.forEach(x -> {
            x.setCreator(null);
        });
        subgoalRepository.saveAll(all_subgoals);
        goal_from_db.getSubgoals().clear();
        goal_from_db.setCreator(null);
        goalRepository.delete(goal_from_db);
        return new MessageResponse("Goal deleted.", MessageType.SUCCESS);
    }

    /************ SUBGOAL CREATION ******/
    public MessageResponse createSubgoal(SubgoalPostDTO subgoalPostDTO) {
        Optional<Goal> goal_opt = goalRepository.findById(subgoalPostDTO.getMain_goal_id());
        if (goal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoalPostDTO);
        new_subgoal.setMainGoal(goal_opt.get());
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setExtension_count(0L);
        new_subgoal.setCreator(goal_opt.get().getCreator());
        new_subgoal.setRating(0D);
        subgoalRepository.save(new_subgoal);
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }


    /********************* EXTEND AND COMPLETE start *************/

    public MessageResponse completeGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        if (goal_from_db.getIsDone()) {
            return new MessageResponse("Already completed!", MessageType.ERROR);
        }
        Set<Subgoal> subgoals = goal_from_db.getSubgoals();
        if (subgoals.stream().filter(x -> !x.getIsDone()).count() > 0) {
            return new MessageResponse("This goal has some subgoals that are uncompleted! Finish those first!", MessageType.ERROR);
        }
        goal_from_db.setIsDone(Boolean.TRUE);
        goal_from_db.setCompletedAt(new Date(System.currentTimeMillis()));
        goal_from_db.setRating(subgoals.stream().map(x -> x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
        goalRepository.save(goal_from_db);
        return new MessageResponse("Goal completed successfully!", MessageType.SUCCESS);
    }

    private static Stream<Subgoal> flatMapRecursive(Subgoal item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(GoalService::flatMapRecursive));
    }
    /********************* EXTEND AND COMPLETE finish *************/

    /********************* ANALYTICS **************/
    public GoalAnalyticsDTO getAnalytics(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        GoalAnalyticsDTO goalAnalyticsDTO = new GoalAnalyticsDTO();

        goalAnalyticsDTO.setGoal_id(goal_id);
        goalAnalyticsDTO.setExtensionCount(goal_from_db.getExtension_count());
        goalAnalyticsDTO.setStartTime(goal_from_db.getCreatedAt());
        if (goal_from_db.getIsDone()) {
            goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.COMPLETED);
            goalAnalyticsDTO.setRating(goal_from_db.getRating());
            goalAnalyticsDTO.setFinishTime(goal_from_db.getCompletedAt());
            goalAnalyticsDTO.setCompletionTimeInMiliseconds(goal_from_db.getCompletedAt().getTime() - goal_from_db.getCreatedAt().getTime());

            List<Goal> common_goals = goalRepository.findAllByCreatedAtIsBetweenOrCompletedAtBetween(goal_from_db.getCreatedAt(), goal_from_db.getCompletedAt(), goal_from_db.getCreatedAt(), goal_from_db.getCompletedAt()).stream().filter(x -> x.getCreator().equals(goal_from_db.getCreator())).collect(Collectors.toList());
            common_goals.remove(goal_from_db);
            goalAnalyticsDTO.setGoalsWithCommonLifetime(goalShortMapper.mapToDto(common_goals).stream().collect(Collectors.toSet()));

        } else {
            goalAnalyticsDTO.setRating(goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).map(x ->
                    x.getRating()).mapToDouble(Double::doubleValue).summaryStatistics().getAverage());
            goalAnalyticsDTO.setStatus(GoalAnalyticsDTO.Status.ACTIVE);
            List<Goal> common_goals = goalRepository.findAllByCreatedAtIsBetweenOrCompletedAtBetween(goal_from_db.getCreatedAt(), new Date(System.currentTimeMillis()), goal_from_db.getCreatedAt(), new Date(System.currentTimeMillis())).stream().filter(x -> x.getCreator().equals(goal_from_db.getCreator())).collect(Collectors.toList());
            common_goals.remove(goal_from_db);
            goalAnalyticsDTO.setGoalsWithCommonLifetime(goalShortMapper.mapToDto(common_goals).stream().collect(Collectors.toSet()));
        }
        if (goal_from_db.getSubgoals().size() > 0) {
            Optional<Subgoal> subgoal_longest_opt = goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime()));
            if (subgoal_longest_opt.isPresent()) {
                goalAnalyticsDTO.setLongestSubgoal(goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).map(x -> subgoalShortMapper.mapToDto(x)).get());
                goalAnalyticsDTO.setShortestSubgoal(subgoalShortMapper.mapToDto(goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).min(Comparator.comparing(x -> x.getCompletedAt().getTime() - x.getCreatedAt().getTime())).get()));
            }
            Optional<Subgoal> subgoal_best_opt = goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(Subgoal::getRating));
            if (subgoal_best_opt.isPresent()) {
                goalAnalyticsDTO.setBestSubgoal(subgoalShortMapper.mapToDto(goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).max(Comparator.comparing(Subgoal::getRating)).get()));
                goalAnalyticsDTO.setWorstSubgoal(subgoalShortMapper.mapToDto(goal_from_db.getSubgoals().stream().filter(z -> z.getIsDone()).min(Comparator.comparing(Subgoal::getRating)).get()));
            }
            goalAnalyticsDTO.setAverageCompletionTimeOfSubgoals((long) goal_from_db.getSubgoals().stream().filter(x -> x.getCompletedAt() != null).map(x ->
                    x.getCompletedAt().getTime() - x.getCreatedAt().getTime()).mapToLong(Long::longValue).summaryStatistics().getAverage());
        }
        goalAnalyticsDTO.setActiveSubgoalCount(goal_from_db.getSubgoals().stream().filter(x -> !x.getIsDone()).count());
        goalAnalyticsDTO.setCompletedSubgoalCount(goal_from_db.getSubgoals().stream().filter(x -> x.getIsDone()).count());

        return goalAnalyticsDTO;
    }

}
