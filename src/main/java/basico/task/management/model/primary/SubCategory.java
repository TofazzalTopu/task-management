package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Entity
@Table(name = "SUB_CATEGORY")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUB_CATEGORY_SEQ")
    @SequenceGenerator(name = "SUB_CATEGORY_SEQ", sequenceName = "SUB_CATEGORY_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    private String code;

    private String status;

    private String capex;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLIER_ID", nullable = true, foreignKey = @ForeignKey(name = "TASK_SUPPLIER_FK"))
    private UserProfile supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "CATEGORY_FK"))
    private Category category;

    @Column(name = "FILES")
    private byte[] file;

    @NotNull
    @Column(name = "FILE_NAME")
    private String fileName;

    @JsonIgnore
    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "FILE_UUID")
    private String fileUUID;

}
