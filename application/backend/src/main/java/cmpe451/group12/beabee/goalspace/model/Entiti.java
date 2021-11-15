package cmpe451.group12.beabee.goalspace.model;

import cmpe451.group12.beabee.goalspace.enums.EntitiType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Entiti {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @JsonIgnoreProperties({"entities"})
//    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "goal_id")
    private Goal mainGoal;


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

    /*
    @JsonIgnoreProperties({"id"})
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Uplinks")
    @Column(name = "uplink_id")
    private Set<Entiti> uplinks;
     */

    @JsonIgnoreProperties({"id"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Sublinks")
    @Column(name = "sublink_id")
    private Set<Entiti> sublinks;

}
