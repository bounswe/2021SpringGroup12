package cmpe451.group12.beabee.goalspace.model;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;


@Data
@Entity
public class Routine extends Entiti{

    private Long period;

    private Date deadline;

    private Double rating;
}
