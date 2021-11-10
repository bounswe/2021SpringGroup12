package cmpe451.group12.beabee.goalspace.model;

import cmpe451.group12.beabee.common.model.Users;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;


    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;
    
    @JsonIgnoreProperties({"mainGoal"})
    @OneToMany(mappedBy = "mainGoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entiti> entities;

}