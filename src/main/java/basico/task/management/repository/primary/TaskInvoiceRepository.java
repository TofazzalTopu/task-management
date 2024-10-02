package basico.task.management.repository.primary;

import basico.task.management.model.primary.TaskInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskInvoiceRepository  extends JpaRepository<TaskInvoice,Long> {

    List<TaskInvoice> findByTaskId(Long id);
}
