package cmpe451.group12.beabee.common.model;

import cmpe451.group12.beabee.goalspace.model.Goal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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

    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<Goal> goals;

    /*
    @JsonIgnoreProperties({"creator"})
    @OneToMany(mappedBy = "creator")
    private Set<Entity> entities;

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
