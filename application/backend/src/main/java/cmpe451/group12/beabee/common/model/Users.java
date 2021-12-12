package cmpe451.group12.beabee.common.model;

import cmpe451.group12.beabee.goalspace.model.entities.Entiti;
import cmpe451.group12.beabee.goalspace.model.goals.Goal;
import cmpe451.group12.beabee.goalspace.model.goals.GroupGoal;
import cmpe451.group12.beabee.goalspace.model.goals.Subgoal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
public class Users{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE/*, generator = "idgen"*/)
    @Column(name = "USER_ID")
    private Long user_id;

    @Column(name = "email")
    private String email;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;


    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;


    @Column(name = "password_reset_token")
    private String password_reset_token;
    @Column(name = "password_reset_token_expiration_date")
    private Date password_reset_token_expiration_date;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<Goal> goals;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<Goal> subgoals;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<GroupGoal> groupgoals;

    @JsonIgnoreProperties({"members"})
    @ManyToMany(mappedBy = "members")
    private Set<GroupGoal> memberOf;

    @JsonIgnoreProperties({"assignees"})
    @ManyToMany(mappedBy = "assignees")
    private Set<Subgoal> assigned;


    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<Entiti> entities;

    /*
    @JsonIgnoreProperties({"id"})
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Person_Followers")
    private Set<Users> followers;

    @JsonIgnoreProperties({"id"})
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="Person_Followings")
    private Set<Users> following;

    private Analytics analytic_report;
*/
}
