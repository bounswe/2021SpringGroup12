package cmpe451.group12.beabee.goalspace.model.activitystreams;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Create_Schema")
@EntityListeners(AuditingEntityListener.class)
public class CreateSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schema")
    private Long id;

    private String context = "https://www.w3.org/ns/activitystreams";
    private String summary;
    private final ActionType type=ActionType.CREATE;
    //private Map<String, String> actor;
    @ManyToOne
    private ActorSchema actor;
    //private Map<String, String> object;
    @ManyToOne
    private ObjectSchema object;
    @CreatedDate
    @Column(name = "createdAt",updatable = false)
    private Date createdAt;
}
