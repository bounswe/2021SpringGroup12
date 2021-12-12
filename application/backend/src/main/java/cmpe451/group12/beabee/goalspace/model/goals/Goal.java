package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
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
    @OneToMany(mappedBy = "goal", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Entiti> entities;

    private Double rating;

    @JsonIgnoreProperties({"mainGroupgoal, mainGoal"})
    @OneToMany(mappedBy = "mainGoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subgoal> subgoals;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Goal)) {
            return false;
        }
        Goal other = (Goal) o;
        return (other.id == this.id) && (this.rating == other.rating) ;
    }
}