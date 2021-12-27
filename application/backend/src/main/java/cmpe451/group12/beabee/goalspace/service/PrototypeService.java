package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.EntitiPrototypeRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.GoalPrototypeRespository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.SubgoalPrototypeRepository;
import cmpe451.group12.beabee.goalspace.dto.goals.GoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.prototypes.EntitiPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.dto.prototypes.SubgoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.mapper.prototypes.*;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.goalspace.model.prototypes.EntitiPrototype;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import cmpe451.group12.beabee.goalspace.model.prototypes.SubgoalPrototype;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PrototypeService {
    private final GoalPrototypeRespository goalPrototypeRespository;
    private final SubgoalPrototypeRepository subgoalPrototypeRepository;
    private final EntitiPrototypeRepository entitiPrototypeRepository;
    private final GoalPrototypeMapper goalPrototypeMapper;
    private final SubgoalPrototypeMapper subgoalPrototypeMapper;
    private final EntitiPrototypeMapper entitiPrototypeMapper;
    private final SubgoalPrototypeShortMapper subgoalPrototypeShortMapper;
    private final EntitiPrototypeShortMapper entitiPrototypeShortMapper;
    private final GoalRepository goalRepository;
    private final GoalService goalService;
    private final TagRepository tagRepository;


    /***************************** PROTOTYPES *********************/
    public List<GoalPrototypeDTO> getPrototypes() {
        List<GoalPrototypeDTO> result = new ArrayList<>();
        goalPrototypeRespository.findAll().stream().map(prototype->prototype.getId()).forEach(id -> {
            result.add(getAPrototype(id));
        });
        return result;
    }

    public GoalPrototypeDTO getAPrototype(Long id) {
        GoalPrototype prototype = goalPrototypeRespository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        GoalPrototypeDTO prototypeDTO = goalPrototypeMapper.mapToDto(prototype);
        prototypeDTO.setEntities(entitiPrototypeShortMapper.mapToDto(prototype.getEntities()));
        prototypeDTO.setSubgoals(subgoalPrototypeShortMapper.mapToDto(prototype.getSubgoals()));
        return  prototypeDTO;
    }

    private Set<EntitiPrototype> clearEntities(Set<Entiti> entities, GoalPrototype prototype) {
        Set<EntitiPrototype> entitiPrototypes = new HashSet<>();
        entities.stream().forEach(entiti->{
            EntitiPrototype entitiPrototype = new EntitiPrototype();
            entitiPrototype.setTitle(entiti.getTitle());
            entitiPrototype.setDescription(entiti.getDescription());
            entitiPrototype.setReference_entiti_id(entiti.getId());
            entitiPrototype.setMainGoal(prototype);
            entitiPrototype.setEntitiType(entiti.getEntitiType());
            entitiPrototypeRepository.save(entitiPrototype);
            entitiPrototypes.add(entitiPrototype);
        });
        return entitiPrototypes;
    }

    private Set<SubgoalPrototype> clearSubgoals(Set<Subgoal> subgoals, GoalPrototype prototype) {
        Set<SubgoalPrototype> subgoals_protos = new HashSet<>();
        subgoals.stream().forEach(subgoal->{
            SubgoalPrototype subgoal1 = new SubgoalPrototype();
            subgoal1.setTitle(subgoal.getTitle());
            subgoal1.setDescription(subgoal.getDescription());
            subgoal1.setReference_subgoal_id(subgoal.getId());
            subgoal1.setMainGoal(prototype);
            subgoalPrototypeRepository.save(subgoal1);
            subgoals_protos.add(subgoal1);
        });
        return subgoals_protos;
    }
    private Set<Tag> clearTags(Set<Tag> tags, GoalPrototype prototype) {
        Set<Tag> new_tags = new HashSet<>();
        new_tags.addAll(tags);
        new_tags.stream().forEach(x->{x.getGoals().clear();x.setGoal_prototypes(Stream.of(prototype).collect(Collectors.toSet()));});
        return new_tags;
    }

    public MessageResponse publishAGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        goal_from_db.getEntities().forEach(System.out::println);
        GoalPrototype prototype = new GoalPrototype();
        goalPrototypeRespository.save(prototype);
        prototype.setReference_goal_id(goal_id);
        prototype.setTags(clearTags(goal_from_db.getTags(),prototype));
        prototype.setEntities(clearEntities(goal_from_db.getEntities(),prototype));
        prototype.setSubgoals(clearSubgoals(goal_from_db.getSubgoals(),prototype));
        //prototype.setGoalType(goal_from_db.getGoalType());
        prototype.setDescription(goal_from_db.getDescription());
        prototype.setTitle(goal_from_db.getTitle());
        goalPrototypeRespository.save(prototype);

        return new MessageResponse("Goal published successfully!", MessageType.SUCCESS);
    }

    public EntitiPrototypeDTO getAnEntitiPrototype(Long id) {
        EntitiPrototype prototype = entitiPrototypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        EntitiPrototypeDTO prototypeDTO = entitiPrototypeMapper.mapToDto(prototype);
        prototypeDTO.setChild_entities(entitiPrototypeMapper.mapToDto(prototype.getChildEntities()));
        return  prototypeDTO;
    }

    public SubgoalPrototypeDTO getASubgoalPrototype(Long id) {
        SubgoalPrototype prototype = subgoalPrototypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        SubgoalPrototypeDTO prototypeDTO = subgoalPrototypeMapper.mapToDto(prototype);
        prototypeDTO.setChild_subgoals(subgoalPrototypeMapper.mapToDto(prototype.getChild_subgoals()));
        return  prototypeDTO;
    }

    /************* SEARCH **********/
    public List<GoalPrototypeDTO> searchGoalPrototypesExact(String query){
        Set<Tag> matched_tags = tagRepository.findAllByNameContains(query);
        Set<GoalPrototype> all_goals = new HashSet<>();
        matched_tags.stream().forEach(x -> {
            all_goals.addAll(goalPrototypeRespository.findAllByTagsIsContaining(x));
        });
        all_goals.addAll(goalPrototypeRespository.findAllByDescriptionContainsOrTitleContains(query, query));

        return goalPrototypeMapper.mapToDto(all_goals.stream().collect(Collectors.toList()));
    }

    public List<GoalPrototypeDTO> searchGoalPrototypesUsingTag(String tag) throws IOException, ParseException {
        Set<String> related_ids = goalService.findRelatedTagIds(Stream.of(tag).collect(Collectors.toSet()));
        Set<Tag> related_tags = new HashSet<>();
        for (String id : related_ids) {
            if (id == null) continue;
            Optional<Tag> tag_x = goalService.getTagById(id);
            if (tag_x.isPresent()) {
                related_tags.add(tag_x.get());
            }
        }

        System.out.println(related_tags.stream().map(x -> x.getName()).collect(Collectors.toList()));
        Set<GoalPrototype> all_prototypes = new HashSet<>();
        related_tags.stream().forEach(x -> {
            all_prototypes.addAll(goalPrototypeRespository.findAllByHiddentagsIsContaining(x));
            all_prototypes.addAll(goalPrototypeRespository.findAllByTagsIsContaining(x));
        });
        List<GoalPrototypeDTO> prototypeDTOS = goalPrototypeMapper.mapToDto(all_prototypes.stream().collect(Collectors.toList()));
        return prototypeDTOS;
    }

}
