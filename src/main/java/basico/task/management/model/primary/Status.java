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
@Table(name = "status")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "STATUS_SEQ")
    @SequenceGenerator(name = "STATUS_SEQ", sequenceName = "status_sequence", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 30)
    private String name;

    @Column(name = "STATUS_DESCRIPTION", length = 30)
    private String description;

    @Column(name = "ENTITY", length = 20)
    private String entity;

    @Column(name = "SEQUENCE", length = 3)
    private String sequence;

}
