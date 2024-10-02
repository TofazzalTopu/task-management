package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TaskDocumentDto {

    @NotNull
    private MultipartFile file;

    @NotNull
    private Long taskId;

    private boolean initialDoc;

}
