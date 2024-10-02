package basico.task.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardDTO {
    List<MonthWiseTaskDTO> monthWiseTaskCountList;
    List<MonthWiseAggregatedTotalDTO> monthWiseAggregatedTotalList;
    List<DisaggregatedTotalCostDTO> disaggregatedTotalCostList;
    double averageCost;
    double averageCompletionTime;
    NoOfTaskCompletedAndPendingDTO noOfTaskCompletedAndPending;
    TaskPendingApprovalDTO taskPendingApproval;
    TaskPendingAssignmentDTO taskPendingAssignment;
}
