package cmpe451.group12.beabee.goalspace.model.activitystreams;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Actor_Schema")
public class ActorSchema {
    enum Type{
        PERSON;
    }
    private final Type type=Type.PERSON;
    @Id
    private String name;
}
