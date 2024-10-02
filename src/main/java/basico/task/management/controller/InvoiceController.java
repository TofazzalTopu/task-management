package basico.task.management.controller;

import basico.task.management.dto.InvoiceDto;
import basico.task.management.dto.InvoiceGenerationDto;
import basico.task.management.dto.InvoiceRejectDto;
import basico.task.management.dto.Response;
import basico.task.management.enums.Status;
import basico.task.management.model.primary.Task;
import basico.task.management.service.BudgetService;
import basico.task.management.service.InvoiceService;
import basico.task.management.service.TaskService;
import basico.task.management.util.CSVGenerator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Locale;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final MessageSource messages;

    private final TaskService taskService;

    private final BudgetService budgetService;


    @ApiOperation(value = "Reject Invoice")
    @PostMapping(value = "/reject/{taskId}/{invoiceNumber}")
    public ResponseEntity<Response> rejectInvoice(@PathVariable @NotNull Long taskId, @PathVariable @NotNull String invoiceNumber, @RequestBody InvoiceRejectDto invoiceRejectDto, @RequestHeader("Authorization") String token, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Task task = taskService.updateStatus(taskId, Status.INVOICE_REJECTED_SUPPLIER.name(), locale);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messages.getMessage("invoice.reject", null, locale), invoiceService.rejectInvoice(invoiceNumber, invoiceRejectDto, token, locale)));
    }

    @ApiOperation(value = "Reject Invoice by tech user")
    @PutMapping(value = "/reject/techUser/{taskId}/{invoiceNumber}")
    public ResponseEntity<Response> techUserRejectInvoice(@PathVariable @NotNull Long taskId, @PathVariable @NotNull String invoiceNumber, @RequestHeader("Authorization") String token, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Task task = taskService.updateStatus(taskId, Status.INVOICE_REJECT_TECH_USER.name(), locale);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messages.getMessage("invoice.reject", null, locale), invoiceService.rejectInvoiceTechUser(invoiceNumber, token, locale)));
    }


    @ApiOperation(value = "Accept Invoice by tech user")
    @PutMapping(value = "/accept/techUser/{taskId}/{invoiceNumber}")
    public ResponseEntity<Response> techUserAcceptInvoice(@PathVariable @NotNull Long taskId, @PathVariable @NotNull String invoiceNumber, @RequestHeader("Authorization") String token, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Task task = taskService.updateStatus(taskId, Status.INVOICE_ACCEPTED_TECH_USER.name(), locale);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messages.getMessage("invoice.accept", null, locale), invoiceService.acceptInvoiceTechUser(invoiceNumber, token, locale)));
    }

    @ApiOperation(value = "Accept Invoice")
    @PostMapping(value = "/accept/{taskId}")
    public ResponseEntity<Response> acceptInvoice(@PathVariable @NotNull Long taskId, @ModelAttribute InvoiceDto invoiceDto, @RequestHeader("Authorization") String token, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Task task = taskService.updateStatus(taskId, Status.INVOICE_UPLOADED.name(), locale);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messages.getMessage("invoice.accept", null, locale), invoiceService.acceptInvoice(task, invoiceDto, token, locale)));
    }

    @ApiOperation(value = "generate invoice pdf")
    @GetMapping(value = "/generate/csv/{taskId}/{invoiceNumber}")
    public ResponseEntity<Object> generateCsv(@PathVariable Long taskId, @PathVariable String invoiceNumber, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {

        Task task = taskService.findById(taskId, locale);
        InvoiceGenerationDto invoiceGenerationDto = invoiceService.invoiceGenerationValue(task, invoiceNumber, budgetService.findByReferenceId(taskId), locale);
        CSVGenerator csvGenerator = new CSVGenerator();
        String str = csvGenerator.csvGenerator(task, invoiceGenerationDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=invoice.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(str);
    }

    @ApiOperation(value = "generate invoice pdf")
    @GetMapping(value = "/generate/pdf/{taskId}/{invoiceNumber}")
    public ResponseEntity<FileSystemResource> acceptInvoice(@PathVariable Long taskId, @PathVariable String invoiceNumber, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Task task = taskService.findById(taskId, locale);
        FileSystemResource file = invoiceService.generateInvoice(task, invoiceNumber, budgetService.findByReferenceId(taskId), locale);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "attachment;filename=" + invoiceNumber + "pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);

    }

}
