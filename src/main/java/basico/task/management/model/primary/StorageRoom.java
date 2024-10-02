package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "STORAGE_ROOM")
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageRoom {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "PROMOTION_ID")
    private Promotion promotion;

    @OneToOne
    @JoinColumn(name = "SOCIETY_ID")
    private Society society;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "RENT")
    private Float rent;

}
