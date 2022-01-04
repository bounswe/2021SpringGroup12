package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.EntitiPrototypeRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.GoalPrototypeRespository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.SubgoalPrototypeRepository;
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
    private final ActivityStreamService activityStreamService;
    private final UserRepository userRepository;

    /***************************** PROTOTYPES *********************/
    public List<GoalPrototypeDTO> getPrototypes() {
        List<GoalPrototypeDTO> result = new ArrayList<>();
        goalPrototypeRespository.findAll().stream().map(prototype -> prototype.getId()).forEach(id -> {
            GoalPrototypeDTO dto = getAPrototype(id);
            if (dto != null && dto.getDownload_count() != null) {
                result.add(dto);
            }
        });
        return result.stream().sorted((i1, i2) -> i2.getDownload_count().compareTo(i1.getDownload_count())).collect(Collectors.toList());
    }

    public List<GoalPrototypeDTO> getPrototypesOfAUser(Long user_id) {
        Users users = userRepository.findById(user_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<GoalPrototypeDTO> result = new ArrayList<>();
        goalPrototypeRespository.findAll().stream().map(prototype -> prototype.getId()).forEach(id -> {
            GoalPrototypeDTO dto = getAPrototype(id);
            if (dto != null && dto.getDownload_count() != null) {
                result.add(dto);
            }
        });
        return result.stream().filter(x -> x.getUsername().equals(users.getUsername())).sorted((i1, i2) -> i2.getDownload_count().compareTo(i1.getDownload_count())).collect(Collectors.toList());
    }

    private static Stream<SubgoalPrototype> flatMapRecursiveSubgoal(SubgoalPrototype item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChild_subgoals())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(PrototypeService::flatMapRecursiveSubgoal));
    }

    private static Stream<EntitiPrototype> flatMapRecursiveEntiti(EntitiPrototype item) {
        return Stream.concat(Stream.of(item), Optional.ofNullable(item.getChildEntities())
                .orElseGet(Collections::emptySet)
                .stream()
                .flatMap(PrototypeService::flatMapRecursiveEntiti));
    }

    public GoalPrototypeDTO getAPrototype(Long id) {
        GoalPrototype prototype = goalPrototypeRespository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        GoalPrototypeDTO prototypeDTO = goalPrototypeMapper.mapToDto(prototype);
        Set<EntitiPrototype> entities = prototype.getEntities().stream()
                .flatMap(PrototypeService::flatMapRecursiveEntiti).collect(Collectors.toSet());
        Set<SubgoalPrototype> subgoals = prototype.getSubgoals().stream()
                .flatMap(PrototypeService::flatMapRecursiveSubgoal).collect(Collectors.toSet());
        prototypeDTO.setEntities(entitiPrototypeShortMapper.mapToDto(entities));
        prototypeDTO.setSubgoals(subgoalPrototypeShortMapper.mapToDto(subgoals));
        try {
            prototypeDTO.setUsername(goalRepository.findById(prototype.getReference_goal_id()).get().getCreator().getUsername());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        prototypeDTO.setDownload_count(goalRepository.findById(prototype.getReference_goal_id()).get().getDownloadCount());
        return prototypeDTO;
    }

    /***** PUBLISH A GOAL******/
    private Set<EntitiPrototype> clearEntities(Set<Entiti> entities, GoalPrototype prototype) {
        Set<EntitiPrototype> entitiPrototypes = new HashSet<>();
        entities.stream().forEach(entiti -> {
            EntitiPrototype entitiPrototype = new EntitiPrototype();
            entitiPrototype.setTitle(entiti.getTitle());
            entitiPrototype.setDescription(entiti.getDescription());
            entitiPrototype.setReference_entiti_id(entiti.getId());
            entitiPrototype.setMainGoal(prototype);
            entitiPrototype.setPeriod(7L);
            entitiPrototype.setChildEntities(clearEntities(entiti.getSublinked_entities(), null));

            entitiPrototype.setEntitiType(entiti.getEntitiType());
            entitiPrototypeRepository.save(entitiPrototype);
            entitiPrototypes.add(entitiPrototype);
        });
        return entitiPrototypes;
    }

    private Set<SubgoalPrototype> clearSubgoals(Set<Subgoal> subgoals, GoalPrototype prototype) {
        Set<SubgoalPrototype> subgoals_protos = new HashSet<>();
        subgoals.stream().forEach(subgoal -> {
            SubgoalPrototype subgoal1 = new SubgoalPrototype();
            subgoal1.setTitle(subgoal.getTitle());
            subgoal1.setDescription(subgoal.getDescription());
            subgoal1.setReference_subgoal_id(subgoal.getId());
            subgoal1.setChild_subgoals(clearSubgoals(subgoal.getChild_subgoals(), null));
            subgoal1.setMainGoal(prototype);
            subgoalPrototypeRepository.save(subgoal1);
            subgoals_protos.add(subgoal1);
        });
        return subgoals_protos;
    }

    private Set<Tag> clearTags(Set<Tag> tags, Goal goal, GoalPrototype prototype) {
        Set<Tag> new_tags = new HashSet<>();
        new_tags.addAll(tags);
        new_tags.stream().forEach(x -> {
            x.getGoals().remove(goal);
            x.getGoal_prototypes().add(prototype);
        });
        return new_tags;
    }

    public MessageResponse publishAGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        GoalPrototype prototype;
        Boolean is_republish = Boolean.FALSE;
        if (goal_from_db.getIsPublished()) {
            prototype = goalPrototypeRespository.findByReference_goal_id(goal_from_db.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.CONFLICT, "Goal is published but prototype does not exist! Data is conflicted!"));
            goalPrototypeRespository.delete(prototype);
            is_republish = Boolean.TRUE;
        }
        prototype = new GoalPrototype();
        goalPrototypeRespository.save(prototype);

        prototype.setReference_goal_id(goal_id);
        prototype.setTags(clearTags(goal_from_db.getTags(), goal_from_db, prototype));
        prototype.setHiddentags(clearTags(goal_from_db.getHiddentags(), goal_from_db, prototype));
        prototype.setEntities(clearEntities(goal_from_db.getEntities(), prototype));
        prototype.setSubgoals(clearSubgoals(goal_from_db.getSubgoals(), prototype));
        //prototype.setGoalType(goal_from_db.getGoalType());
        prototype.setDescription(goal_from_db.getDescription());
        prototype.setTitle(goal_from_db.getTitle());
        goalPrototypeRespository.save(prototype);
        goal_from_db.setIsPublished(Boolean.TRUE);
        goalRepository.save(goal_from_db);
        if (is_republish) {
            activityStreamService.republishGoalSchema(goal_from_db.getCreator(), prototype);
        } else {
            activityStreamService.publishGoalSchema(goal_from_db.getCreator(), prototype);
        }
        return new MessageResponse("Goal published successfully!", MessageType.SUCCESS);
    }

    public MessageResponse unpublishAGoal(Long goal_id) {
        Goal goal_from_db = goalRepository.findById(goal_id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!"));
        if (!goal_from_db.getIsPublished()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Goal is not published yet!");
        }
        GoalPrototype prototype = goalPrototypeRespository.findByReference_goal_id(goal_id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal prototype not found!"));
        goalPrototypeRespository.delete(prototype);
        goal_from_db.setIsPublished(Boolean.FALSE);
        goalRepository.save(goal_from_db);
        return new MessageResponse("Prototype unpublished successfully.", MessageType.SUCCESS);
    }

    public EntitiPrototypeDTO getAnEntitiPrototype(Long id) {
        EntitiPrototype prototype = entitiPrototypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        EntitiPrototypeDTO prototypeDTO = entitiPrototypeMapper.mapToDto(prototype);
        prototypeDTO.setChild_entities(entitiPrototypeMapper.mapToDto(prototype.getChildEntities()));
        return prototypeDTO;
    }

    public SubgoalPrototypeDTO getASubgoalPrototype(Long id) {
        SubgoalPrototype prototype = subgoalPrototypeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prototype not found!"));
        SubgoalPrototypeDTO prototypeDTO = subgoalPrototypeMapper.mapToDto(prototype);
        prototypeDTO.setChild_subgoals(subgoalPrototypeMapper.mapToDto(prototype.getChild_subgoals()));
        return prototypeDTO;
    }

    /************* SEARCH **********/
    public List<GoalPrototypeDTO> searchGoalPrototypesExact(String query) {
        Set<Tag> matched_tags = tagRepository.findAllByNameContains(query);
        Set<GoalPrototype> all_prototypes = new HashSet<>();
        matched_tags.stream().forEach(x -> {
            all_prototypes.addAll(goalPrototypeRespository.findAllByTagsIsContaining(x));
        });
        all_prototypes.addAll(goalPrototypeRespository.findAllByDescriptionContainsOrTitleContains(query, query));
        List<GoalPrototypeDTO> result = new ArrayList<>();
        all_prototypes.stream().forEach(prototype -> {
            GoalPrototypeDTO goalPrototypeDTO = new GoalPrototypeDTO();
            Optional<Goal> ref_goal = goalRepository.findById(prototype.getReference_goal_id());
            if (ref_goal.isPresent()) {
                goalPrototypeDTO.setDownload_count(ref_goal.get().getDownloadCount());
                goalPrototypeDTO.setId(prototype.getId());
                Set<EntitiPrototype> entities = prototype.getEntities().stream()
                        .flatMap(PrototypeService::flatMapRecursiveEntiti).collect(Collectors.toSet());
                Set<SubgoalPrototype> subgoals = prototype.getSubgoals().stream()
                        .flatMap(PrototypeService::flatMapRecursiveSubgoal).collect(Collectors.toSet());
                goalPrototypeDTO.setEntities(entitiPrototypeShortMapper.mapToDto(entities));
                goalPrototypeDTO.setSubgoals(subgoalPrototypeShortMapper.mapToDto(subgoals));

                goalPrototypeDTO.setReference_goal_id(prototype.getReference_goal_id());
                goalPrototypeDTO.setTitle(prototype.getTitle());
                goalPrototypeDTO.setDescription(prototype.getDescription());
                Set<Tag> set2 = prototype.getTags();
                if (set2 != null) {
                    goalPrototypeDTO.setTags(set2.stream().map(x -> x.getName()).collect(Collectors.toSet()));
                }
                try {
                    goalPrototypeDTO.setUsername(goalRepository.getById(prototype.getReference_goal_id()).getCreator().getUsername());
                    result.add(goalPrototypeDTO);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        return result.stream().sorted((i1, i2) -> i2.getDownload_count().compareTo(i1.getDownload_count())).collect(Collectors.toList());
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

        Set<GoalPrototype> all_prototypes = new HashSet<>();
        related_tags.stream().forEach(x -> {
            all_prototypes.addAll(goalPrototypeRespository.findAllByHiddentagsIsContaining(x));
            all_prototypes.addAll(goalPrototypeRespository.findAllByTagsIsContaining(x));
        });
        List<GoalPrototypeDTO> prototypeDTOS = goalPrototypeMapper.mapToDto(all_prototypes.stream().collect(Collectors.toList()));
        prototypeDTOS.stream().forEach(prototypeDTO -> {
            Optional<Goal> goal = goalRepository.findById(prototypeDTO.getReference_goal_id());
            if (goal.isPresent()) {
                GoalPrototype prototype = goalPrototypeRespository.getById(prototypeDTO.getId());
                Set<EntitiPrototype> entities = prototype.getEntities().stream()
                        .flatMap(PrototypeService::flatMapRecursiveEntiti).collect(Collectors.toSet());
                Set<SubgoalPrototype> subgoals = prototype.getSubgoals().stream()
                        .flatMap(PrototypeService::flatMapRecursiveSubgoal).collect(Collectors.toSet());
                prototypeDTO.setEntities(entitiPrototypeShortMapper.mapToDto(entities));
                prototypeDTO.setSubgoals(subgoalPrototypeShortMapper.mapToDto(subgoals));
                prototypeDTO.setDownload_count(goal.get().getDownloadCount());
                prototypeDTO.setUsername(goal.get().getCreator().getUsername());
            }
        });
        return prototypeDTOS.stream().sorted((i1, i2) -> i2.getDownload_count().compareTo(i1.getDownload_count())).collect(Collectors.toList());

    }


}
