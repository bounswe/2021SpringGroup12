package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Subgoal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen2")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "main_goal_id")
    private Goal mainGoal;

    @Column(name = "title")
    private String title;
    @Column(name = "isDone")
    private Boolean isDone;
    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "createdAt",updatable = false)
    private Date createdAt;

    @Column(name = "deadline")
    private Date deadline;

    @JsonIgnoreProperties({"subgoal"})
    @OneToMany(mappedBy = "subgoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entiti> entities;

    @JsonIgnoreProperties({"id"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Subgoals")
    @Column(name = "parent_subgoal_id")
    private Set<Subgoal> child_subgoals;

    private Double rating;


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
        Subgoal geek = (Subgoal) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.id == this.id);
    }
}
