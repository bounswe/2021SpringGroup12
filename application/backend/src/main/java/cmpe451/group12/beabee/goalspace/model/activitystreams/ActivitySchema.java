package cmpe451.group12.beabee.goalspace.model.activitystreams;


import cmpe451.group12.beabee.goalspace.enums.ActivityType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Activity_Schema")
@EntityListeners(AuditingEntityListener.class)
public class ActivitySchema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schema")
    private Long id;

    private String context = "https://www.w3.org/ns/activitystreams";
    private String summary;
    private ActivityType type;

    @ManyToOne
    private ActorSchema actor;
    @ManyToOne
    private ObjectSchema objectschema;//leave,join,follow,create

    private String object;//remove,update,delete,add

    @ManyToOne
    private ObjectSchema target;//remove,add(publish)

    @ManyToOne
    private OriginSchema origin;//delete,add(publish)

    @CreatedDate
    @Column(name = "createdAt",updatable = false)
    private Date createdAt;

}
