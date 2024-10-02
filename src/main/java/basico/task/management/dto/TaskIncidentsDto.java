package basico.task.management.dto;

import basico.task.management.util.ValidDate;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class TaskIncidentsDto {

    @NotEmpty
    @NonNull
    private String comment;

    @NonNull
    private Long taskId;

    @NonNull
    private Long supplierId;

    @NonNull
    @ValidDate
    private String deliveryDate;


}
