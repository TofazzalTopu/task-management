package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "GROUP")
public class Group {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="GROUP_SEQ")
    @SequenceGenerator(name="GROUP_SEQ", sequenceName="GROUP_SEQ", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    private String groupName;


}
