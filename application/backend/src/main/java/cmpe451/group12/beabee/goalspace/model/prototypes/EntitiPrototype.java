package cmpe451.group12.beabee.goalspace.model.prototypes;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class EntitiPrototype {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;


    @Column(name = "reference_id")
    private Long reference_entiti_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_goal_id")
    private GoalPrototype mainGoal;

    @Column(name = "entityType")
    private EntitiType entitiType;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "period")
    private Long period;
    @JsonIgnoreProperties({"id"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "EntitiPrototypeParentship")
    @Column(name = "child_entiti_id")
    private Set<EntitiPrototype> childEntities;

    @Override
    public boolean equals(Object obj)
    {

        // checking if both the object references are
        // referring to the same object.
        if(this == obj)
            return true;

        // it checks if the argument is of the
        // type Geek by comparing the classes
        // of the passed argument and this object.
        // if(!(obj instanceof Geek)) return false; ---> avoid.
        if(obj == null || obj.getClass()!= this.getClass())
            return false;

        // type casting of the argument.
        EntitiPrototype geek = (EntitiPrototype) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.id == this.id);
    }
    @Override
    public String toString() {
        return  "Proto_id: " + this.id;
    }
}
