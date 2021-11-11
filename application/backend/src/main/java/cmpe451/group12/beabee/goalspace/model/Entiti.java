package cmpe451.group12.beabee.goalspace.model;

import cmpe451.group12.beabee.goalspace.enums.EntityType;
import cmpe451.group12.beabee.goalspace.enums.GoalType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Entiti {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @JsonIgnoreProperties({"entities"})
//    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "goal_id")
    private Goal mainGoal;


    @Column(name = "entityType")
    private EntityType entityType;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "createdAt")
    private Date createdAt;
    @Column(name = "isDone")
    private Boolean isDone;


    /*
    @JsonIgnoreProperties({"id"})
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Uplinks")
    @Column(name = "uplink_id")
    private Set<Entiti> uplinks;
     */

    @JsonIgnoreProperties({"id"})
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Sublinks")
    @Column(name = "sublink_id")
    private Set<Entiti> sublinks;

}
