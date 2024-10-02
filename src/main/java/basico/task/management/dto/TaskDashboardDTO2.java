package basico.task.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDashboardDTO2 {
    List<DayWiseTaskDTO> pendingList;
    List<DayWiseTaskDTO> pendingApprovalList;
    List<DayWiseTaskDTO> completedList;
}
