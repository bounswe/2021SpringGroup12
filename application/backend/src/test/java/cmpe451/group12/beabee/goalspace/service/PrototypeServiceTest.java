package cmpe451.group12.beabee.goalspace.service;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.entities.*;
import cmpe451.group12.beabee.goalspace.Repository.goals.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.goals.TagRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.EntitiPrototypeRepository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.GoalPrototypeRespository;
import cmpe451.group12.beabee.goalspace.Repository.prototypes.SubgoalPrototypeRepository;
import cmpe451.group12.beabee.goalspace.dto.prototypes.GoalPrototypeDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiShortMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.*;
import cmpe451.group12.beabee.goalspace.mapper.prototypes.*;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PrototypeServiceTest {
    private PrototypeService prototypeService;

    private GoalPrototypeRespository goalPrototypeRespository;
    private SubgoalPrototypeRepository subgoalPrototypeRepository;
    private EntitiPrototypeRepository entitiPrototypeRepository;
    private GoalPrototypeMapper goalPrototypeMapper;
    private SubgoalPrototypeMapper subgoalPrototypeMapper;
    private EntitiPrototypeMapper entitiPrototypeMapper;
    private SubgoalPrototypeShortMapper subgoalPrototypeShortMapper;
    private EntitiPrototypeShortMapper entitiPrototypeShortMapper;
    private GoalRepository goalRepository;
    private GoalService goalService;
    private TagRepository tagRepository;

    @Before
    public void setUp() {
        goalPrototypeRespository = Mockito.mock(GoalPrototypeRespository.class);
        subgoalPrototypeRepository = Mockito.mock(SubgoalPrototypeRepository.class);
        entitiPrototypeRepository = Mockito.mock(EntitiPrototypeRepository.class);
        goalPrototypeMapper = Mockito.mock(GoalPrototypeMapper.class);
        subgoalPrototypeMapper = Mockito.mock(SubgoalPrototypeMapper.class);
        entitiPrototypeMapper = Mockito.mock(EntitiPrototypeMapper.class);
        subgoalPrototypeShortMapper = Mockito.mock(SubgoalPrototypeShortMapper.class);
        entitiPrototypeShortMapper = Mockito.mock(EntitiPrototypeShortMapper.class);
        goalRepository = Mockito.mock(GoalRepository.class);
        goalService = Mockito.mock(GoalService.class);
        tagRepository = Mockito.mock(TagRepository.class);

        prototypeService = new PrototypeService(goalPrototypeRespository, subgoalPrototypeRepository,
                entitiPrototypeRepository, goalPrototypeMapper, subgoalPrototypeMapper,
                entitiPrototypeMapper, subgoalPrototypeShortMapper, entitiPrototypeShortMapper,
                goalRepository, goalService, tagRepository);
    }

    @Test
    public void searchGoalPrototypesExact_ShouldReturnMathcedPrototypes() {
        /**
         * Search query: h
         * proto1: title has "h"
         * proto 2: no relation in tags, title or description
         * proto3: has two tags that contain "h"
         */
        /************ MOCK DATA*/
        /* MODELS */
        String search_query = "h";
        GoalPrototype prototype1 = new GoalPrototype();
        prototype1.setTitle("Hello there!");
        GoalPrototype prototype2 = new GoalPrototype();
        prototype2.setDescription("So uncivilized.!");
        Tag tag1 = new Tag();
        tag1.setName("have");
        Tag tag2 = new Tag();
        tag2.setName("high");
        Tag tag3 = new Tag();
        tag3.setName("ground");
        GoalPrototype prototype3 = new GoalPrototype();
        prototype3.setTags(Stream.of(tag1, tag2, tag3).collect(Collectors.toSet()));
        Users user = new Users();
        user.setUsername("obi1");
        Goal ref_goal = new Goal();
        ref_goal.setCreator(user);
        ref_goal.setId(1L);
        Users user2 = new Users();
        user2.setUsername("obi2");
        Goal ref_goal2 = new Goal();
        ref_goal2.setId(2L);
        ref_goal2.setCreator(user2);

        /* DTOS */
        GoalPrototypeDTO prototypeDTO1 = new GoalPrototypeDTO();
        prototypeDTO1.setTitle("Hello there!");
        GoalPrototypeDTO prototypeDTO3 = new GoalPrototypeDTO();
        prototypeDTO3.setTags(Stream.of(tag1, tag2, tag3).map(x -> x.getName()).collect(Collectors.toSet()));
        prototypeDTO1.setReference_goal_id(ref_goal.getId());
        prototypeDTO3.setReference_goal_id(ref_goal2.getId());

        /*  MOCKS */
        Mockito.when(tagRepository.findAllByNameContains(search_query)).thenReturn(Stream.of(tag1, tag2).collect(Collectors.toSet()));
        Mockito.when(goalPrototypeRespository.findAllByTagsIsContaining(tag1)).thenReturn(Stream.of(prototype3).collect(Collectors.toList()));
        Mockito.when(goalPrototypeRespository.findAllByTagsIsContaining(tag2)).thenReturn(Stream.of(prototype3).collect(Collectors.toList()));
        Mockito.when(goalPrototypeRespository.findAllByDescriptionContainsOrTitleContains(search_query, search_query)).thenReturn(Stream.of(prototype1).collect(Collectors.toList()));
        Mockito.when(goalPrototypeMapper.mapToDto(Stream.of(prototype1, prototype3).collect(Collectors.toList()))).thenReturn(Stream.of(prototypeDTO1, prototypeDTO3).collect(Collectors.toList()));
        Mockito.when(goalRepository.findById(1L)).thenReturn(Optional.of(ref_goal));
        Mockito.when(goalRepository.findById(2L)).thenReturn(Optional.of(ref_goal2));

        List<GoalPrototypeDTO> result = prototypeService.searchGoalPrototypesExact(search_query);
        List<GoalPrototypeDTO> expected = Stream.of(prototypeDTO1, prototypeDTO3).collect(Collectors.toList());
        Assert.assertEquals(result, expected);

        Mockito.verify(tagRepository).findAllByNameContains(search_query);
        Mockito.verify(goalPrototypeRespository).findAllByTagsIsContaining(tag1);
        Mockito.verify(goalPrototypeRespository).findAllByTagsIsContaining(tag2);
        Mockito.verify(goalPrototypeRespository).findAllByDescriptionContainsOrTitleContains(search_query, search_query);
        Mockito.verify(goalPrototypeMapper).mapToDto(Stream.of(prototype1, prototype3).collect(Collectors.toList()));
        Mockito.verify(goalRepository).findById(1L);
        Mockito.verify(goalRepository).findById(2L);
    }

    @Test
    public void searchGoalPrototypesUsingTag_ShouldReturnMatchedPrototypes() throws IOException, ParseException {
        /**
         * search query: man
         * proto1 directly contains the tag: woman.
         * proto2 contains a tag which shares a similar tag with query: person.
         * proto3 is contains the similar tag: person.
         * proto4 has no relation at all.
         *
         * Expected return: [proto1, proto2, proto3]
         */


        /************* MOCK DATA*/
        /* MODELS */
        String search_query = "man";
        GoalPrototype prototype1 = new GoalPrototype();
        prototype1.setId(1L);
        GoalPrototype prototype2 = new GoalPrototype();
        prototype2.setId(2L);
        GoalPrototype prototype3 = new GoalPrototype();
        prototype3.setId(3L);
        GoalPrototype prototype4 = new GoalPrototype();
        prototype4.setId(4L);

        Tag tag1 = new Tag();
        tag1.setId("Q1");
        tag1.setName("woman");
        Tag tag2 = new Tag();
        tag2.setId("Q2");
        tag2.setName("dog");
        Tag tag3 = new Tag();
        tag3.setId("Q3");
        tag3.setName("person");
        Tag tag4 = new Tag();
        tag3.setId("Q4");
        tag4.setName("window");

        prototype1.setTags(Stream.of(tag1).collect(Collectors.toSet()));// only have woman
        prototype2.setTags(Stream.of(tag2).collect(Collectors.toSet()));// only have dog
        prototype3.setTags(Stream.of(tag3).collect(Collectors.toSet()));// only have person
        prototype4.setTags(Stream.of(tag4).collect(Collectors.toSet()));// only have window
        prototype1.setHiddentags(Stream.of(tag3).collect(Collectors.toSet()));
        prototype2.setHiddentags(Stream.of(tag3).collect(Collectors.toSet()));
        prototype3.setHiddentags(Stream.of(tag1, tag2).collect(Collectors.toSet()));
        /* Observe that tag1 and tag2 are not directly related. They have a common similar tag: tag3*/
        Users user = new Users();
        user.setUsername("obi1");
        Goal ref_goal = new Goal();
        ref_goal.setCreator(user);
        ref_goal.setId(1L);

        /* DTOS */
        GoalPrototypeDTO prototypeDTO1 = new GoalPrototypeDTO();
        prototypeDTO1.setTags(Stream.of(tag1.getName()).collect(Collectors.toSet()));
        GoalPrototypeDTO prototypeDTO2 = new GoalPrototypeDTO();
        prototypeDTO2.setTags(Stream.of(tag2.getName()).collect(Collectors.toSet()));
        GoalPrototypeDTO prototypeDTO3 = new GoalPrototypeDTO();
        prototypeDTO3.setTags(Stream.of(tag3).map(x -> x.getName()).collect(Collectors.toSet()));

        /*  MOCKS */
        Mockito.when(goalService.findRelatedTagIds(Stream.of(search_query).collect(Collectors.toSet()))).thenReturn(Stream.of("Q3").collect(Collectors.toSet()));//only related field is person
        Mockito.when(goalService.getTagById("Q3")).thenReturn(Optional.of(tag3));
        Mockito.when(goalPrototypeRespository.findAllByHiddentagsIsContaining(tag3)).thenReturn(Stream.of(prototype1, prototype2).collect(Collectors.toSet()));
        Mockito.when(goalPrototypeRespository.findAllByTagsIsContaining(tag3)).thenReturn(Stream.of(prototype3).collect(Collectors.toList()));
        Mockito.when(goalPrototypeMapper.mapToDto(Stream.of(prototype2, prototype3, prototype1).collect(Collectors.toList()))).thenReturn(Stream.of(prototypeDTO1, prototypeDTO2, prototypeDTO3).collect(Collectors.toList()));
        Mockito.when(goalRepository.findById(null)).thenReturn(Optional.of(ref_goal));

        List<GoalPrototypeDTO> result = prototypeService.searchGoalPrototypesUsingTag(search_query);
        List<GoalPrototypeDTO> expected = Stream.of(prototypeDTO1, prototypeDTO2, prototypeDTO3).collect(Collectors.toList());
        Assert.assertEquals(result, expected);

        Mockito.verify(goalService).findRelatedTagIds(Stream.of(search_query).collect(Collectors.toSet()));
        Mockito.verify(goalService).getTagById("Q3");
        Mockito.verify(goalPrototypeRespository).findAllByHiddentagsIsContaining(tag3);
        Mockito.verify(goalPrototypeRespository).findAllByTagsIsContaining(tag3);
        Mockito.verify(goalPrototypeMapper).mapToDto(Stream.of(prototype2, prototype3, prototype1).collect(Collectors.toList()));

    }

}
