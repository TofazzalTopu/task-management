package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.BudgetDTO;
import basico.task.management.dto.BudgetResponse;
import basico.task.management.dto.EmailDTO;
import basico.task.management.enums.Status;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.Budget;
import basico.task.management.model.primary.Role;
import basico.task.management.model.primary.Task;
import basico.task.management.repository.primary.BudgetRepository;
import basico.task.management.service.*;
import basico.task.management.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.*;

import static basico.task.management.constant.AppConstants.SUBMIT_BUDGET;
import static basico.task.management.constant.AppConstants.SUBMIT_BUDGET_TEMPLATE;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final SubCategoryService subCategoryService;
    private final TaskService taskService;
    private final MessageSource messageSource;
    private final MailSender mailSender;
    private final RoleService roleService;
    private final TaskInvoiceService taskInvoiceService;

    @Value("${tax.percentage}")
    private double taxPercentage;

    @Value("${spring.mail.username}")
    private String systemEmail;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${backend.url}")
    private String backendUrl;

    @Override
    public List<Budget> save(String token, Long referenceId, String type, List<BudgetDTO> budgetDTOList, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        budgetRepository.deleteAll(findByReferenceIdAndType(token, referenceId, type, locale).getBudget());
        List<Budget> budgetList = new ArrayList<>();
        budgetDTOList.forEach(dto -> {
            Budget budget = new Budget();
            budget.setReferenceId(referenceId);
            budget.setType(type);
            budget.setQuantity(dto.getQuantity());
            budget.setUnit(dto.getUnit());
            budget.setPrice(dto.getPrice());
            budget.setSubCategory(subCategoryService.findById(dto.getSubCategoryId(), locale));
            budget.setCreatedDate(LocalDate.now());
            budget.setComment(dto.getComment());
            budget.setFianza(dto.isFianza());
            budget = budgetRepository.save(budget);
            budgetList.add(budget);
        });

        Task task = taskService.updateStatusToSubmitBudget(referenceId, locale);
        if (Objects.nonNull(task.getTechnicalAssignee()) && Objects.nonNull(task.getTechnicalAssignee().getEmail())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), "", SUBMIT_BUDGET, SUBMIT_BUDGET_TEMPLATE);
        }
        if (Objects.nonNull(task.getSupplier()) && Objects.nonNull(task.getSupplier().getEmail())) {
            sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), SUBMIT_BUDGET, SUBMIT_BUDGET_TEMPLATE);
        }
        return budgetList;
    }

    @Override
    public BudgetResponse findByReferenceIdAndType(String token, Long referenceId, String type, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        BudgetResponse budgetResponse = new BudgetResponse();
        budgetResponse.setBudget(budgetRepository.findAllByReferenceIdAndType(referenceId, type));
        Task task = taskService.findById(referenceId, locale);
        budgetResponse.setTax(Objects.nonNull(task.getTaxPercentage()) ? task.getTaxPercentage() : taxPercentage);
        if (task.getStatus().getName().equals(Status.TASK_COMPLETED.name())) {
            budgetResponse.setInvoiceNumber(taskInvoiceService.findByTaskId(task.getId()));
        }
        return budgetResponse;
    }

    @Override
    public Budget findById(String token, Long id, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        return budgetRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("budget.not.found", null, locale)));
    }

    @Override
    public void delete(String token, Long id, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        budgetRepository.deleteById(id);
    }

    @Override
    public List<Budget> findByReferenceId(Long id) {
        return budgetRepository.findAllByReferenceId(id);
    }

    public void sendEmail(Task task, String email, String name, String providerName, String subject, String template) throws MessagingException {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTo(email);

        emailDTO.setFrom(systemEmail);
        emailDTO.setSubject(subject);
        emailDTO.setTemplate(template);
        Map<String, Object> properties = new HashMap<>();
        properties.put("taskId", task.getTaskId());
        properties.put("name", name);
        properties.put("providerName", providerName);
        properties.put("logo", backendUrl + "/photo.png");
        properties.put("photo", backendUrl + "/logo-white.png");
        properties.put("url", domainUrl + "/tasks/view/" + task.getType() + "/" + task.getId());
        emailDTO.setProperties(properties);
        mailSender.sendEmail(emailDTO);
    }
}
