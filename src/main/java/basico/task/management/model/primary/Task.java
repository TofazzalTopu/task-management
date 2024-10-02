package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_SEQ")
    @SequenceGenerator(name = "TASK_SEQ", sequenceName = "TASK_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TASK_ID", length = 20)
    private String taskId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "REFERENCE_ID")
    private Long referenceId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CAPEX")
    private boolean capex;

    @Column(name = "OPEX")
    private boolean opex;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "TASK_SUPPLIER_FK"))
    private UserProfile supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TECHNICAL_ASSIGNEE_ID", foreignKey = @ForeignKey(name = "TASK_TECHNICAL_ASSIGNEE_FK"))
    private UserProfile technicalAssignee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY_ID", nullable = false, foreignKey = @ForeignKey(name = "TASK_CREATED_BY_FK"))
    private UserProfile createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "task_status_fk"))
    private Status status;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "APPROVED_DATE")
    private LocalDate approvedDate;

    @Column(name = "NO_OF_DAYS")
    @Max(value = 365)
    @Min(value = 1)
    private Integer noOfDays;

    @Column(name = "ASSIGN_DATE")
    private LocalDate assignDate;

    @Column(name = "DELIVERY_DATE")
    private LocalDate deliveryDate;

    @Column(name = "IS_APPROVE")
    private Boolean isApprove;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "TAX_PERCENTAGE")
    private Double taxPercentage;

    @Column(name = "INVOICE_DATE")
    private LocalDate invoiceDate;

    @Column(name = "CODE")
    private String code;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "DIRECTION")
    private String direction;

    @Column(name = "CITY")
    private String city;

    @Column(name = "ARR_OPERACION")
    private String arrOperacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_EXIT")
    private Date dateExit;

    @Column(name = "DATE_SIGN")
    private String dateSign;

    @Transient
    List<Comment> comments;

    @Transient
    private List<Invoice> invoice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SOCIETYID", foreignKey = @ForeignKey(name = "TASK_SOCIETYID_FK"))
    private Society society;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTIONID", foreignKey = @ForeignKey(name = "TASK_PROMOTIONID_FK"))
    private Promotion promotion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GARAJE_ID", foreignKey = @ForeignKey(name = "TASK_GARAJE_ID_FK"))
    private Garaje garajes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORAGE_ID", foreignKey = @ForeignKey(name = "TASK_STORAGE_ID_FK"))
    private StorageRoom storageRoom;

    @Column(name = "ASSET_ID")
    private String assetId;


}
