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


    @Column(name = "password_reset_token")
    private String password_reset_token;
    @Column(name = "password_reset_token_expiration_date")
    private Date password_reset_token_expiration_date;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator",cascade = CascadeType.REMOVE, orphanRemoval=true)
    private Set<Goal> goals;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator",cascade = CascadeType.PERSIST)
    private Set<Goal> subgoals;

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator",orphanRemoval=true)
    private Set<GroupGoal> groupgoals;

    @JsonIgnoreProperties({"members"})
    @ManyToMany(mappedBy = "members")
    private Set<GroupGoal> memberOf;

    @JsonIgnoreProperties({"assignees"})
    @ManyToMany(mappedBy = "assignees")
    private Set<Subgoal> assigned;


    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator",orphanRemoval=true)
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
        Users geek = (Users) obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (geek.user_id == this.user_id);
    }
    @Override
    public String toString() {
        return  "Goal_id: " + this.user_id;
    }
}
