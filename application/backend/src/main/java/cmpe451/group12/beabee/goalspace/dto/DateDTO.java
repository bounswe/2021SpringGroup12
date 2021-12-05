package cmpe451.group12.beabee.goalspace.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import java.util.Date;

@Getter
@Setter
public class DateDTO {

    /*TODO: Not working*/
    @JsonProperty("newDeadline")
    @Future(message = "Başlangıç tarihi bugünden sonra olmalı!")
    private Date newDeadline;

}
