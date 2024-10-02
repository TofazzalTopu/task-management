package basico.task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoOfTaskCompletedAndPendingDTO {
    long completed;
    long pending;

}
