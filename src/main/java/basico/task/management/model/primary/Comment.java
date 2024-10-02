package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ")
    @SequenceGenerator(name = "COMMENT_SEQ", sequenceName = "COMMENT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TASK_ID", length = 20)
    private Long taskId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY_ID", nullable = false, foreignKey = @ForeignKey(name = "COMMENT_CREATED_BY_ID_FK"))
    private UserProfile createdBy;

    @Column(name = "COMMENTS")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "comments_task_status_fk"))
    private Status status;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

}
