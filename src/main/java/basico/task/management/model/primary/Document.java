package basico.task.management.model.primary;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENT_SEQ")
    @SequenceGenerator(name = "DOCUMENT_SEQ", sequenceName = "DOCUMENT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "DATES")
    private Date date;

    @Column(name = "FILES")
    private byte[] file;

    @Column(name = "DOC_TYPE")
    private String docType;

    @JsonIgnore
    @Column(name = "FILE_PATH")
    private String filePath;

    @Column(name = "FILE_UUID")
    private String fileUUID;

    @Column(name = "MIME_TYPE")
    private String mimeType;

    @Column(name = "INITIAL_DOC")
    private boolean initialDoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "document_user_fk"))
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false, foreignKey = @ForeignKey(name = "document_status_fk"))
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TASK_ID", nullable = false, foreignKey = @ForeignKey(name = "TASK_FK"))
    private Task task;

}
