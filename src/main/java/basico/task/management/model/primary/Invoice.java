package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "INVOICE")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Invoice_SEQ")
    @SequenceGenerator(name = "Invoice_SEQ", sequenceName = "Invoice_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICE_DATE")
    private LocalDate invoiceDate;

    @Column(name = "APPROVE_DATE")
    private LocalDate approveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID", nullable = false, foreignKey = @ForeignKey(name = "status_fk"))
    private Status invoiceStatue;

    @Transient
    private byte[] invoiceDoc;

    @Transient
    private String fileUUID;

    @Transient
    private String fileName;


}
