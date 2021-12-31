package cmpe451.group12.beabee.goalspace.model.activitystreams;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Object_Schema")
public class ObjectSchema {
    public enum Type{
        GOAL,
        GROUPGOAL,
        COPY,
        UPDATE,
        ADD,
        PUBLISH,
        REPUBLISH,
        PERSON;
    }
    private Type type;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schema")
    private Long id;
    private String name;//title
    private String content; //description
    private String url; // on deletion
}
