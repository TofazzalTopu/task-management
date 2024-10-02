package basico.task.management.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryUploadDTO {

    @NotNull
    @NotBlank
    private String categoryName;

    @NotNull
    @NotBlank
    private String subCategoryName;

    private String comment;

}
