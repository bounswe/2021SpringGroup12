package cmpe451.group12.beabee.goalspace.model.activitystreams;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Origin_Schema")
public class OriginSchema {
    enum Type{
        GOAL,
        GROUPGOAL;
    }
    private ObjectSchema.Type type;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schema")
    private Long id;
    private String name;//title

}
