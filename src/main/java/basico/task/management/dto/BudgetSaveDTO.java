package basico.task.management.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BudgetSaveDTO {

    @NotNull
    @NotEmpty
    private List<BudgetDTO> budgetDTOList;

}
