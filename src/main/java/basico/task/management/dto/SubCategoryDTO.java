package basico.task.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SubCategoryDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    private String capex;

    @NotNull
    private Long categoryId;

    @NotNull(message = "Please upload file!")
    private MultipartFile file;
}
