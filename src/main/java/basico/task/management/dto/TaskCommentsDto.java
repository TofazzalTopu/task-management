package basico.task.management.dto;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

@Data
public class TaskCommentsDto {

    @NotEmpty
    @NonNull
    private String comment;

    @NonNull
    private Long taskId;

}
