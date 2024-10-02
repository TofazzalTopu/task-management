package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DocumentDTO {

    @NotNull
    private List<MultipartFile> files;

    @NotNull
    private Long taskId;

}
