package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "budgets")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUDGET_SEQ")
    @SequenceGenerator(name = "BUDGET_SEQ", sequenceName = "BUDGET_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REFERENCE_ID", nullable = false)
    private Long referenceId;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(nullable = false)
    private Double price;

    @NotNull
    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "UNIT", nullable = false)
    private Integer unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUB_CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "BUDGET_SUB_CATEGORY_FK"))
    private SubCategory subCategory;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "CATEGORY_COMMENTS")
    private String comment;

    @Column(name = "FIANZA")
    private boolean fianza = false;

}
