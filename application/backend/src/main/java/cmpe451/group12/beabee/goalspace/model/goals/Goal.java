package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Goal extends AllGoal{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;
    
    @JsonIgnoreProperties({"goal", "groupgoal"})
    @OneToMany(mappedBy = "goal", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY,orphanRemoval=true)
    private Set<Entiti> entities;

    private Double rating;

    @JsonIgnoreProperties({"mainGroupgoal, mainGoal"})
    @OneToMany(mappedBy = "mainGoal",  fetch = FetchType.LAZY,orphanRemoval=true)
    private Set<Subgoal> subgoals;


    @ManyToMany
    @JoinTable(
            name = "goal_tag",
            joinColumns = { @JoinColumn(name = "goal_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "goal_hidden_tag",
            joinColumns = { @JoinColumn(name = "goal_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> hiddentags;

    private Long downloadCount;
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
        Goal geek = (Goal) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.id == this.id);
    }
    @Override
    public String toString() {
        return  "Goal_id: " + this.id;
    }
}