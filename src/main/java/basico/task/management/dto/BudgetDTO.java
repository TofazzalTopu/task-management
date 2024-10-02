package basico.task.management.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BudgetDTO {

    @NotNull
    @NotBlank
    private Long subCategoryId;

    @NotNull
    @NotBlank
    private Double price;

    @NotNull
    @NotBlank
    private Integer quantity;

    @NotNull
    @NotBlank
    private Integer unit;

    private String comment;

    private boolean fianza = false;
}
