package basico.task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisaggregatedTotalCostDTO {
    String type;
    int totalCost;

}
