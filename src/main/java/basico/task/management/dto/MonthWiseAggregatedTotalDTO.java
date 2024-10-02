package basico.task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthWiseAggregatedTotalDTO {
    String year;
    String month;
    int totalCost;

}
