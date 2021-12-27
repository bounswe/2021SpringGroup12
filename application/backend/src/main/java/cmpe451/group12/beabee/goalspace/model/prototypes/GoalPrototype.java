package cmpe451.group12.beabee.goalspace.model.prototypes;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.AllGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.goals.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class GoalPrototype {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @Column(name = "reference_id")
    private Long reference_goal_id;


    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;


    @JsonIgnoreProperties({"mainGoal", "mainGroupgoal"})
    @OneToMany(mappedBy = "mainGoal", fetch = FetchType.LAZY,orphanRemoval=true)
    private Set<EntitiPrototype> entities;

    @JsonIgnoreProperties({"mainGroupgoal, mainGoal"})
    @OneToMany(mappedBy = "mainGoal",  fetch = FetchType.LAZY,orphanRemoval=true)
    private Set<SubgoalPrototype> subgoals;


    @ManyToMany
    @JoinTable(
            name = "goal_prototype_tag",
            joinColumns = { @JoinColumn(name = "tag_id") },
            inverseJoinColumns = { @JoinColumn(name = "goal_prototype_id") }
    )
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "goal_prototype_hidden_tag",
            joinColumns = { @JoinColumn(name = "goal_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> hiddentags;

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
        GoalPrototype geek = (GoalPrototype) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.id == this.id);
    }
    @Override
    public String toString() {
        return  "Proto_id: " + this.id;
    }

}
