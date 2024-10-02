package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "garaje")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Garaje {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID")
    private Promotion promotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCIETY_ID")
    private Society society;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "rent")
    private Float rent;

}
