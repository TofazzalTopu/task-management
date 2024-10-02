package basico.task.management.model.primary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "CATEGORY")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="CATEGORY_SEQ")
    @SequenceGenerator(name="CATEGORY_SEQ", sequenceName="CATEGORY_SEQ", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLIER_ID", nullable = true, foreignKey = @ForeignKey(name = "TASK_SUPPLIER_FK"))
    private UserProfile supplier;

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

    @Transient
    List<SubCategory> subCategoryList;
}
