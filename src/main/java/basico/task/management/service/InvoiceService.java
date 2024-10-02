package basico.task.management.service;

import basico.task.management.dto.InvoiceDto;
import basico.task.management.dto.InvoiceGenerationDto;
import basico.task.management.dto.InvoiceRejectDto;
import basico.task.management.model.primary.Budget;
import basico.task.management.model.primary.Invoice;
import basico.task.management.model.primary.Task;
import org.springframework.core.io.FileSystemResource;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.io.IOException;
import java.util.Locale;

public interface InvoiceService {

    Invoice generateInvoiceNumber(Task task);

    Invoice rejectInvoice(String invoiceNumber, InvoiceRejectDto invoiceRejectDto, String token, Locale locale);

    Invoice acceptInvoice(Task task, InvoiceDto invoiceDto, String token, Locale locale) throws IOException, KeyManagementException, NoSuchAlgorithmException;

    FileSystemResource generateInvoice(Task task, String invoiceNumber, List<Budget> budgets, Locale locale) throws Exception;

    InvoiceGenerationDto invoiceGenerationValue(Task task, String invoiceNumber, List<Budget> budget, Locale locale) throws Exception;

    Invoice rejectInvoiceTechUser(String invoiceNumber, String token, Locale locale);

    Invoice acceptInvoiceTechUser(String invoiceNumber, String token, Locale locale);
}