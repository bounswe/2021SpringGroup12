package cmpe451.group12.beabee.goalspace.service;


import cmpe451.group12.beabee.common.dto.MessageResponse;
import cmpe451.group12.beabee.common.enums.MessageType;
import cmpe451.group12.beabee.common.repository.UserRepository;
import cmpe451.group12.beabee.goalspace.Repository.EntitiRepository;
import cmpe451.group12.beabee.goalspace.Repository.GoalRepository;
import cmpe451.group12.beabee.goalspace.Repository.SubgoalRepository;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalDTOShort;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalGetDTO;
import cmpe451.group12.beabee.goalspace.dto.goals.SubgoalPostDTO;
import cmpe451.group12.beabee.goalspace.mapper.entities.EntitiMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalGetMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalPostMapper;
import cmpe451.group12.beabee.goalspace.mapper.goals.SubgoalShortMapper;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubgoalService {

    private final SubgoalGetMapper subgoalGetMapper;
    private final SubgoalShortMapper subgoalShortMapper;
    private final SubgoalPostMapper subgoalPostMapper;
    private final SubgoalRepository subgoalRepository;
    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final EntitiMapper entitiMapper;
    private final EntitiRepository entitiRepository;


    public MessageResponse createSubgoal(SubgoalPostDTO subgoalPostDTO) {
        Optional<Subgoal> parent_subgoal_opt = subgoalRepository.findById(subgoalPostDTO.getParent_subgoal_id());
        if (parent_subgoal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent subgoal not found!");
        }
        Subgoal new_subgoal = subgoalPostMapper.mapToEntity(subgoalPostDTO);

        // initialize fields of new subgoal
        new_subgoal.setIsDone(Boolean.FALSE);
        new_subgoal.setRating(0D);
        // Add this subgoal to parent subgoal
        Set<Subgoal> subgoals_of_parent = parent_subgoal_opt.get().getChild_subgoals();
        subgoals_of_parent.add(new_subgoal);
        parent_subgoal_opt.get().setChild_subgoals(subgoals_of_parent);
        // save to DB
        subgoalRepository.save(new_subgoal);
        subgoalRepository.save(parent_subgoal_opt.get());
        return new MessageResponse("Subgoal added.", MessageType.SUCCESS);
    }

    public SubgoalGetDTO getSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        if (subgoal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        SubgoalGetDTO subgoalGetDTO = subgoalGetMapper.mapToDto(subgoal_opt.get());
        if (subgoal_opt.get().getMainGoal() != null) {
            subgoalGetDTO.setMain_goal_id(subgoal_opt.get().getMainGoal().getId());
        }
        return subgoalGetDTO;
    }

    /**
     * TODO: sıkıntılı: level-2 altı subgoallere erişmek için recursive bi şey yazmak lazım.
     */
    /*
    public List<SubgoalDTOShort> getSubgoalsOfAUser(Long user_id) {
        if (!userRepository.existsById(user_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        List<Goal> goals = goalRepository.findAllByUserId(user_id);
        List<Subgoal> result = new ArrayList<>();

        for (Goal g : goals) {
            List<Subgoal> subgoals_of_goal_g = subgoalRepository.findByGoalId(g.getId());
            for (Subgoal s : subgoals_of_goal_g) {
                List<Subgoal> subgoals_of_subgoal_s = s.getChild_subgoals().stream().collect(Collectors.toList());
                subgoal_dtos.addAll(subgoalShortMapper.mapToDto(subgoals_of_subgoal_s));
            }
            subgoal_dtos.addAll(subgoalGetMapper.mapToDto(subgoalRepository.findByGoalId(g.getId())).stream().map(x -> {
                x.setMain_goal_id(g.getId());
                return x;
            }).collect(Collectors.toList()));
        }
        return subgoal_dtos;
    }
*/
    public List<SubgoalDTOShort> getSubgoalsOfAGoal(Long goal_id) {
        if (!goalRepository.existsById(goal_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found!");
        }
        return subgoalShortMapper.mapToDto(subgoalRepository.findByGoalId(goal_id));
    }

    public List<SubgoalDTOShort> getSubgoalsOfASubgoal(Long subgoal_id) {
        if (!subgoalRepository.existsById(subgoal_id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        return subgoalShortMapper.mapToDto(subgoalRepository.findById(subgoal_id).get().getChild_subgoals().stream().collect(Collectors.toList()));
    }

    // Works great!
    public MessageResponse deleteSubgoal(Long id) {
        Optional<Subgoal> subgoal_opt = subgoalRepository.findById(id);
        if (subgoal_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        if (subgoal_opt.get().getMainGoal() != null) {
            //level 1 subgoal
            Goal main_goal = subgoal_opt.get().getMainGoal();
            Set<Subgoal> subgoals = main_goal.getSubgoals();
            subgoals.remove(subgoal_opt.get());
            main_goal.setSubgoals(subgoals);
            goalRepository.save(main_goal);
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        } else {
            Subgoal parent_subgoal = subgoalRepository.findParentById(id);
            Set<Subgoal> child_subgoals = parent_subgoal.getChild_subgoals();
            child_subgoals.remove(subgoal_opt.get());
            parent_subgoal.setChild_subgoals(child_subgoals);
            subgoalRepository.save(parent_subgoal);
            subgoalRepository.delete(subgoal_opt.get());
            return new MessageResponse("Subgoal deleted!", MessageType.SUCCESS);
        }
    }

    public MessageResponse updateSubgoal(SubgoalGetDTO subgoal_dto) {
        Optional<Subgoal> subgoal_from_db_opt = subgoalRepository.findById(subgoal_dto.getId());
        if (subgoal_from_db_opt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subgoal not found!");
        }
        if (subgoal_dto.getRating() != null)
            subgoal_from_db_opt.get().setRating(subgoal_dto.getRating());
        if (subgoal_dto.getDeadline() != null)
            subgoal_from_db_opt.get().setDeadline(subgoal_dto.getDeadline());
        if (subgoal_dto.getDescription() != null)
            subgoal_from_db_opt.get().setDescription(subgoal_dto.getDescription());
        if (subgoal_dto.getTitle() != null)
            subgoal_from_db_opt.get().setTitle(subgoal_dto.getTitle());
        if (subgoal_dto.getIsDone() != null)
            subgoal_from_db_opt.get().setIsDone(subgoal_dto.getIsDone());

        subgoalRepository.save(subgoal_from_db_opt.get());
        return new MessageResponse("Updated subgoal", MessageType.SUCCESS);
    }

}
