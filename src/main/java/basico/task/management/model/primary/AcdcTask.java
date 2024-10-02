package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "ACDC_TASK")
public class AcdcTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACDC_SEQ")
    @SequenceGenerator(name = "ACDC_SEQ", sequenceName = "ACDC_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ARR_ID")
    private String arrId;

    @Column(name = "ARR_OPERACION")
    private String arrOperacion;

    @Column(name = "CODE")
    private String code;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "CITY",length = 100)
    private String city;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_EXIT")
    private Date dateExit;

    @Column(name = "DATE_SIGN")
    private String dateSign;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCIETY_ID",foreignKey = @ForeignKey(name = "ACDC_SOCIETY_ID_FK"))
    private Society society;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID",foreignKey = @ForeignKey(name = "ACDC_PROMOTION_ID_FK"))
    private Promotion promotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCATION_ID",foreignKey = @ForeignKey(name = "ACDC_SOCIETY_ID_FK"))
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "status_fk"))
    private Status status;

    @Transient
    private String societyName;




}
