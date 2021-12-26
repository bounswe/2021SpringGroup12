package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.goalspace.model.prototypes.GoalPrototype;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Tag {
    private  String name;

    @Id
    private String id;

    @JsonIgnoreProperties({"tags"})
    @ManyToMany(mappedBy = "tags")
    private Set<Goal> goals;

    @JsonIgnoreProperties({"tags"})
    @ManyToMany(mappedBy = "tags")
    private Set<GoalPrototype> goal_prototypes;
}
