package basico.task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthWiseTaskDTO {
    String year;
    String month;
    int count;

}
