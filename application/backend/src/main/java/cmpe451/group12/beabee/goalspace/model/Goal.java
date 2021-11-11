package cmpe451.group12.beabee.goalspace.model;

import cmpe451.group12.beabee.common.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class Goal extends AllGoal{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
    @Column(name = "ID")
    private Long id;

    // TODO: not working
    @CreatedDate
    @Column(name = "createdAt")
    private Date createdAt;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users creator;
    
    @JsonIgnoreProperties({"mainGoal"})
    @OneToMany(mappedBy = "mainGoal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Entiti> entities;

}