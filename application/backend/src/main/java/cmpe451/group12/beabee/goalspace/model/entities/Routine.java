package cmpe451.group12.beabee.goalspace.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Date;
import java.util.List;


@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class Routine extends Entiti {

    private Long period;

    @ElementCollection
    private List<Date> deadline;
    private Long extension_count;
    @ElementCollection
    private List<Double> rating;
}
