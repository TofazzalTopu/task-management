package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TaskDocumentUpdateDto {

    @NotNull
    private Long taskId;

    @NotNull
    private MultipartFile file;

    private boolean initialDoc;

}
