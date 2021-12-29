package cmpe451.group12.beabee.goalspace.model.entities;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import cmpe451.group12.beabee.goalspace.model.resources.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EqualsAndHashCode
public abstract class Entiti {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @JsonIgnoreProperties({"entities"})
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;

    @JsonIgnoreProperties({"entities"})
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "groupgoal_id")
    private GroupGoal groupgoal;

    @Column(name = "completedAt")
    private Date completedAt;

    @Column(name = "entityType")
    private EntitiType entitiType;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "isDone")
    private Boolean isDone;

    @CreatedDate
    @Column(name = "createdAt", updatable = false)
    private Date createdAt;

    @JsonIgnoreProperties({"id"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Entity_Sublinks")
    @Column(name = "sublink_id")
    private Set<Entiti> sublinked_entities;

    @JsonIgnoreProperties({"sublinked_entities"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Subgoal_Sublinks")
    @Column(name = "sublink_id")
    private Set<Subgoal> sublinked_subgoals;

    @JsonIgnoreProperties({"entiti"})
    @OneToMany(mappedBy = "entiti", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Resource> resources;

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
        Entiti geek = (Entiti) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.id == this.id);
    }

}
