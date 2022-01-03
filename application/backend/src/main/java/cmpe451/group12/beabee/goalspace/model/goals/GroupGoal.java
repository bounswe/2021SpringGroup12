package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.common.model.Users;
import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "GROUP_GOAL")
public class GroupGoal extends AllGoal
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;

    @JsonIgnoreProperties({"id"})
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "GroupGoal_Members",
            joinColumns = { @JoinColumn(name = "groupgoal_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @Column(name = "member_id")
    private Set<Users> members;

    @Column(name = "token")
    private String token;

    @JsonIgnoreProperties({"goal", "groupgoal"})
    @OneToMany(mappedBy = "groupgoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entiti> entities;


    @JsonIgnoreProperties({"mainGroupgoal", "goal"})
    @OneToMany(mappedBy = "mainGroupgoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Subgoal> subgoals;

}
