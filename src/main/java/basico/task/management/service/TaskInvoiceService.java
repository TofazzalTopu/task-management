package basico.task.management.service;

import basico.task.management.model.primary.Invoice;
import basico.task.management.model.primary.Task;
import basico.task.management.model.primary.TaskInvoice;

import java.util.*;

public interface TaskInvoiceService {

    TaskInvoice saveTaskInvoice(Task task, Invoice invoice);

    List<Invoice> findByTaskId(Long id);
}
