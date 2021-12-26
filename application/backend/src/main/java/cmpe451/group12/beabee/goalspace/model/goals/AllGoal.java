package cmpe451.group12.beabee.goalspace.model.goals;

import cmpe451.group12.beabee.goalspace.enums.GoalType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class AllGoal  {

    //@Column(name = "goalType")
    private GoalType goalType;
    @Column(name = "title")
    private String title;
    @Column(name = "isDone")
    private Boolean isDone;
    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "createdAt",updatable = false)
    private Date createdAt;

    @Column(name = "completedAt")
    private Date completedAt;

    @Column(name = "extension_count")
    private Long extension_count;



}
