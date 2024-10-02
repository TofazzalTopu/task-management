package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.InvoiceDto;
import basico.task.management.dto.InvoiceGenerationDto;
import basico.task.management.dto.InvoiceQuantity;
import basico.task.management.dto.InvoiceRejectDto;
import basico.task.management.enums.DocType;
import basico.task.management.enums.Entity;
import basico.task.management.enums.Status;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.*;
import basico.task.management.repository.primary.InvoiceRepository;
import basico.task.management.service.*;
import basico.task.management.util.GeneratePdf;
import basico.task.management.util.TokenUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final MessageSource messages;

    private final StatusService statusService;

    private final DocumentService documentService;

    private final Configuration configuration;

    private final RoleService roleService;

    private final TaskInvoiceService taskInvoiceService;

    private final CommentService commentService;

    private final UserService userService;

    @Override
    public Invoice generateInvoiceNumber(Task task) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setInvoiceStatue(statusService.findByNameAndEntity(Status.INVOICE_PENDING.name(), Entity.TASK.name()));
        invoice.setInvoiceNumber(uniqueInvoiceNumber(task));
        Invoice invoiceSaved = invoiceRepository.save(invoice);
        taskInvoiceService.saveTaskInvoice(task, invoiceSaved);
        return invoiceSaved;
    }

    @Override
    public Invoice rejectInvoice(String invoiceNumber, InvoiceRejectDto invoiceRejectDto, String token, Locale locale) {

        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messages.getMessage("you.are.not.authorized", null, locale));
        }
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        if (Objects.isNull(invoice)) {
            throw new NotFoundException(messages.getMessage("invoice.not.found", null, locale));
        }
        checkDate(invoice.getInvoiceDate(), locale);
        invoice.setApproveDate(LocalDate.now());
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.INVOICE_REJECTED_SUPPLIER.name(), Entity.TASK.name());
        invoice.setInvoiceStatue(status);
        invoiceRepository.save(invoice);
        saveComment(invoiceRejectDto.getTaskId(), Long.parseLong(userId), status, invoiceRejectDto.getComments(), locale);
        return invoice;
    }


    private Comment saveComment(Long taskId, Long createdBy, basico.task.management.model.primary.Status status, String comments, Locale locale) {
        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setStatus(status);
        comment.setCreatedBy(userService.findById(createdBy, locale));
        comment.setComment(comments);
        return commentService.save(comment);
    }


    @Override
    public Invoice acceptInvoice(Task task, InvoiceDto invoiceDto, String token, Locale locale) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messages.getMessage("you.are.not.authorized", null, locale));
        }
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceDto.getInvoiceNumber());
        if (Objects.isNull(invoice)) {
            throw new NotFoundException(messages.getMessage("invoice.not.found", null, locale));
        }
        checkDate(invoice.getInvoiceDate(), locale);
        invoice.setApproveDate(LocalDate.now());
        invoice.setInvoiceStatue(statusService.findByNameAndEntity(Status.INVOICE_UPLOADED.name(), Entity.TASK.name()));
        invoiceRepository.save(invoice);
        documentService.saveDocument(Long.parseLong(userId), invoiceDto, task, DocType.INVOICE.name(), locale);
        return invoice;
    }

    @Override
    public FileSystemResource generateInvoice(Task task, String invoiceNumber, List<Budget> budget, Locale locale) throws Exception {
        GeneratePdf pdf = new GeneratePdf();
        configuration.setClassForTemplateLoading(this.getClass(),
                "/templates");
        Template template = configuration.getTemplate("invoice.ftl");
        StringWriter out = new StringWriter();
        template.process(invoiceGenerationValue(task, invoiceNumber, budget, locale), out);
        return pdf.generatePdf(task, out);
    }


    @Override
    public InvoiceGenerationDto invoiceGenerationValue(Task task, String invoiceNumber, List<Budget> budget, Locale locale) throws Exception {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        List<InvoiceQuantity> invoiceQuantities = new LinkedList<>();
        double amount = 0;
        if (Objects.nonNull(budget)) {
            for (Budget b : budget) {
                InvoiceQuantity invoiceQuantity = new InvoiceQuantity();
                invoiceQuantity.setName(b.getSubCategory().getName());
                invoiceQuantity.setPrice(b.getPrice());
                if (task.getTaxPercentage() > 0) {
                    invoiceQuantity.setAmount(b.getPrice() + ((b.getPrice() * task.getTaxPercentage()) / 100));
                } else {
                    invoiceQuantity.setAmount(b.getPrice());
                }
                invoiceQuantities.add(invoiceQuantity);
                amount = amount + b.getPrice();
            }
        }
        InvoiceGenerationDto invoiceGenerationDto = new InvoiceGenerationDto();
        if (Objects.nonNull(task.getLocation())) {
            invoiceGenerationDto.setLocation(task.getLocation());
        } else {
            invoiceGenerationDto.setLocation("-");
            invoiceGenerationDto.setDirection("-");
        }
        if (Objects.nonNull(task.getAssetId()) && Objects.nonNull(task.getAssetId())) {
            invoiceGenerationDto.setAssetId(task.getAssetId());
        }

        double taxAmount = BigDecimal.valueOf((amount * task.getTaxPercentage()) / 100).setScale(2).doubleValue();
        invoiceGenerationDto.setTaskId(task.getTaskId());
        invoiceGenerationDto.setTaxBase(amount / task.getTaxPercentage());
        invoiceGenerationDto.setTotal(amount + taxAmount);
        invoiceGenerationDto.setSubTotal(amount);
        invoiceGenerationDto.setSupplierId(task.getSupplier().getId());
        invoiceGenerationDto.setSocietyName("-");
        invoiceGenerationDto.setNif(task.getSupplier().getNif());
        invoiceGenerationDto.setSupplierAddress(task.getSociety().getSocietyName());
        invoiceGenerationDto.setSupplierCompanyName(task.getSupplier().getCompany());
        invoiceGenerationDto.setTax(taxAmount);
        invoiceGenerationDto.setInvoiceDate(invoice.getInvoiceDate().toString());
        invoiceGenerationDto.setInvoiceNumber(invoice.getInvoiceNumber());
        invoiceGenerationDto.setQuantities(invoiceQuantities);
        return invoiceGenerationDto;

    }

    @Override
    public Invoice rejectInvoiceTechUser(String invoiceNumber, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messages.getMessage("you.are.not.authorized", null, locale));
        }
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        if (Objects.isNull(invoice)) {
            throw new NotFoundException(messages.getMessage("invoice.not.found", null, locale));
        }
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.INVOICE_REJECT_TECH_USER.name(), Entity.TASK.name());
        invoice.setInvoiceStatue(status);
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice acceptInvoiceTechUser(String invoiceNumber, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messages.getMessage("you.are.not.authorized", null, locale));
        }
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);
        if (Objects.isNull(invoice)) {
            throw new NotFoundException(messages.getMessage("invoice.not.found", null, locale));
        }
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.INVOICE_ACCEPTED_TECH_USER.name(), Entity.TASK.name());
        invoice.setInvoiceStatue(status);
        return invoiceRepository.save(invoice);
    }


    private String uniqueInvoiceNumber(Task t) {
        long maxId = invoiceRepository.findMaxId() + 1;
        return t.getType() + t.getId() + String.format("%07d", maxId);
    }

    private void checkDate(LocalDate dbDate, Locale locale) {
        LocalDate localDate = LocalDate.now();
        long daysBetween = Duration.between(dbDate.atStartOfDay(), localDate.atStartOfDay()).toDays();
        if (daysBetween > 3) {
            throw new UnAuthorizedException(messages.getMessage("update.invoice.expire", null, locale));
        }
    }

}
