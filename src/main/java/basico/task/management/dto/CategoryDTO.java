package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDTO {

    private String name;

    @NotNull(message = "Please upload file!")
    private MultipartFile file;
}
