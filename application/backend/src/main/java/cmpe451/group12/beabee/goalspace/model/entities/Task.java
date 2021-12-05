package cmpe451.group12.beabee.goalspace.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.Date;


@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class Task extends Entiti {

    
    private Date deadline;

    private Long extension_count;

    private Double rating;
}
