package basico.task.management.service.impl;

import basico.task.management.enums.Status;
import basico.task.management.model.primary.Document;
import basico.task.management.model.primary.Invoice;
import basico.task.management.model.primary.Task;
import basico.task.management.model.primary.TaskInvoice;
import basico.task.management.repository.primary.TaskInvoiceRepository;
import basico.task.management.service.DocumentService;
import basico.task.management.service.TaskInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskInvoiceServiceImpl implements TaskInvoiceService {

    private final TaskInvoiceRepository taskInvoiceRepository;
    private final DocumentService documentService;


    @Override
    public TaskInvoice saveTaskInvoice(Task task, Invoice invoice) {
        TaskInvoice taskInvoice = new TaskInvoice();
        taskInvoice.setTask(task);
        taskInvoice.setInvoice(invoice);
        return taskInvoiceRepository.save(taskInvoice);
    }

    @Override
    public List<Invoice> findByTaskId(Long id) {
        List<TaskInvoice> taskInvoices = taskInvoiceRepository.findByTaskId(id);
        List<Invoice> invoices = new ArrayList<>();
        for (TaskInvoice t : taskInvoices) {
            if (t.getInvoice().getInvoiceStatue().getName().equalsIgnoreCase(Status.INVOICE_UPLOADED.name()) ||
                    t.getInvoice().getInvoiceStatue().getName().equalsIgnoreCase(Status.INVOICE_ACCEPTED_TECH_USER.name())) {
                Document document = documentService.findAcceptedInvoiceOfTask(id);
                if (Objects.nonNull(document)) {
                    t.getInvoice().setInvoiceDoc(document.getFile());
                    t.getInvoice().setFileName(document.getName());
                    t.getInvoice().setFileUUID(document.getFileUUID());
                }
            }
            invoices.add(t.getInvoice());
        }
        return invoices;
    }
}
