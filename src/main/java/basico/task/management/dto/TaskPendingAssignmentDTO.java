package basico.task.management.dto;

import basico.task.management.model.primary.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskPendingAssignmentDTO {
    Page<Task> pendingAssignmentList;
}
