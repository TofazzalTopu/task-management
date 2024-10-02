package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.*;
import basico.task.management.dto.userprofile.CompanyDto;
import basico.task.management.enums.Entity;
import basico.task.management.enums.Status;
import basico.task.management.exception.AlreadyExistException;
import basico.task.management.exception.NotAllowedException;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.*;
import basico.task.management.repository.primary.TaskRepository;
import basico.task.management.service.*;
import basico.task.management.util.Numeric;
import basico.task.management.util.TokenUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static basico.task.management.constant.AppConstants.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final AssetsService assetService;
    private final AcdcTaskService acdcTaskService;
    private final TaskInvoiceService taskInvoiceService;
    private final GlpiTaskService glpiTaskService;
    private final UserService userService;
    private final RoleService roleService;
    private final MailSender mailSender;
    private final DocumentService documentService;
    private final StatusService statusService;
    private final CommentService commentService;
    private final MessageSource messageSource;
    private final InvoiceService invoiceService;
    private final SocietyService societyService;
    private final GarajeService garajeService;
    private final PromotionService promotionService;
    private final StorageRoomService storageRoomService;
    private final JdbcTemplate jdbcTemplate;

    @Value("${tax.percentage}")
    private double taxPercentage;

    @Value("${spring.mail.username}")
    private String systemEmail;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${backend.url}")
    private String backendUrl;

    @Override
    public Task save(TaskDto taskDto, String token, Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }

        if (Objects.nonNull(taskDto.getReferenceId())) {
            checkReferenceIdAndType(taskDto.getReferenceId(), taskDto.getType(), locale);
            Task assetTask = findTaskByReferenceIdAndType(taskDto.getReferenceId(), taskDto.getType());
            if (Objects.nonNull(assetTask)) {
                throw new AlreadyExistException(messageSource.getMessage("task.already.exit", null, locale));
            }
        }
        Task task = new Task();
        long maxId = taskRepository.findMaxId() + 1;
        String taskId = taskDto.getType() + String.format("%07d", maxId);
        task.setTaskId(taskId);
        task.setType(taskDto.getType());
        task.setDescription(taskDto.getDescription());
        task.setCapex(taskDto.isCapex());
        task.setOpex(taskDto.isOpex());
        if (Objects.nonNull(taskDto.getTechnicalAssignee())) {
            task.setTechnicalAssignee(userService.findById(taskDto.getTechnicalAssignee(), locale));
            task.setStatus(statusService.findByNameAndEntity(Status.ASSIGNED.name(), Entity.TASK.name()));
        }
        if (Objects.nonNull(taskDto.getSupplierId())) {
            task.setSupplier(userService.findById(taskDto.getSupplierId(), locale));
            task.setStatus(statusService.findByNameAndEntity(Status.DRAFT.name(), Entity.TASK.name()));
        }
        if (Objects.isNull(taskDto.getSupplierId()) && Objects.isNull(taskDto.getTechnicalAssignee())) {
            task.setStatus(statusService.findByNameAndEntity(Status.DRAFT.name(), Entity.TASK.name()));
        }
        task.setReferenceId(taskDto.getReferenceId());
        task.setLocation(taskDto.getLocation());
        task.setNoOfDays(taskDto.getNoOfDays());
        if (Objects.nonNull(taskDto.getAssetId()))
            task.setAssetId(taskDto.getAssetId());
        if (Objects.nonNull(taskDto.getStorageRoom()) && !taskDto.getStorageRoom().isEmpty()) {
            task.setStorageRoom(storageRoomService.findById(taskDto.getStorageRoom().trim(), locale));
        }
        if (Objects.nonNull(taskDto.getGaraje()) && !taskDto.getGaraje().isEmpty()) {
            task.setGarajes(garajeService.findById(taskDto.getGaraje().trim(), locale));
        }
        if (Objects.nonNull(taskDto.getSocietyId())) {
            task.setSociety(societyService.findById(taskDto.getSocietyId()));
        }
        if (Objects.nonNull(taskDto.getPromotionId())) {
            task.setPromotion(promotionService.findById(taskDto.getPromotionId()));
        }
        task.setDeliveryDate(LocalDate.parse(taskDto.getDeliveryDate()));
        task.setCode(taskDto.getCode());
        task.setProvince(taskDto.getProvince());
        task.setDirection(taskDto.getDirection());
        task.setCity(taskDto.getCity());
        task.setArrOperacion(taskDto.getArrOperacion());
        task.setDateExit(taskDto.getDateExit());
        task.setDateSign(taskDto.getDateSign());
        task.setCreatedBy(userService.findById(taskDto.getCreatedById(), locale));
        task.setReferenceId(taskDto.getReferenceId());
        task.setCreatedDate(LocalDate.now());
        Task savedTask = taskRepository.save(task);
        if (!taskDto.getTaskDocumentDtoList().isEmpty()) {
            documentService.saveAll(Long.parseLong(userId), taskDto.getTaskDocumentDtoList(), task, locale);
        }
        if (!StringUtils.isEmpty(taskDto.getComments()) && !taskDto.getComments().equals("")) {
            saveComment(savedTask.getId(), Long.parseLong(userId), task.getStatus(), taskDto.getComments(), locale);
        }
        savedTask.setComments(commentService.findAllByTaskId(savedTask.getId()));
        if (Objects.nonNull(savedTask.getTechnicalAssignee())) {
            sendEmail(savedTask, savedTask.getTechnicalAssignee().getEmail(), savedTask.getTechnicalAssignee().getUserName(), "", ASSIGN_TASK_TO_TECHNICAL_USER, ASSIGN_TASK_TO_TECHNICAL_USER_TEMPLATE);
        }
        if (Objects.nonNull(savedTask.getSupplier())) {
            sendEmail(savedTask, savedTask.getSupplier().getEmail(), savedTask.getSupplier().getUserName(), savedTask.getSupplier().getUserName(), ASSIGN_TO_SUPPLIER, ASSIGN_TO_SUPPLIER_TEMPLATE);
        }
        return savedTask;
    }

    @Override
    public Task update(Long id, TaskUpdateDto taskUpdateDto, String token, Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }

        if (Objects.nonNull(taskUpdateDto.getReferenceId())) {
            checkReferenceIdAndType(taskUpdateDto.getReferenceId(), taskUpdateDto.getType(), locale);
        }
        Task task = findById(id, locale);

        if (!role.getLabel().equalsIgnoreCase(ROLE_LABEL_SUPPLIER) && task.getCreatedBy().getId().equals(Long.parseLong(userId))) {
            task.setType(taskUpdateDto.getType());
            if (Objects.nonNull(taskUpdateDto.getTechnicalAssignee()))
                task.setTechnicalAssignee(userService.findById(taskUpdateDto.getTechnicalAssignee(), locale));
            if (Objects.nonNull(taskUpdateDto.getSupplierId()))
                task.setSupplier(userService.findById(taskUpdateDto.getSupplierId(), locale));
            task.setReferenceId(taskUpdateDto.getReferenceId());
            task.setLocation(taskUpdateDto.getLocation());
            if (Objects.nonNull(taskUpdateDto.getStorageRoom()) && !taskUpdateDto.getStorageRoom().isEmpty()) {
                task.setStorageRoom(storageRoomService.findById(taskUpdateDto.getStorageRoom().trim(), locale));
            }
            if (Objects.nonNull(taskUpdateDto.getGaraje()) && !taskUpdateDto.getGaraje().isEmpty()) {
                task.setGarajes(garajeService.findById(taskUpdateDto.getGaraje().trim(), locale));
            }
            if (Objects.nonNull(taskUpdateDto.getSocietyId())) {
                task.setSociety(societyService.findById(taskUpdateDto.getSocietyId()));
            }
            if (Objects.nonNull(taskUpdateDto.getPromotionId())) {
                task.setPromotion(promotionService.findById(taskUpdateDto.getPromotionId()));
            }
            if (Objects.nonNull(taskUpdateDto.getAssetId()))
                task.setAssetId(taskUpdateDto.getAssetId());
            task.setNoOfDays(taskUpdateDto.getNoOfDays());
            task.setCode(taskUpdateDto.getCode());
            task.setDeliveryDate(LocalDate.parse(taskUpdateDto.getDeliveryDate()));
            task.setProvince(taskUpdateDto.getProvince());
            task.setDirection(taskUpdateDto.getDirection());
            task.setCity(taskUpdateDto.getCity());
            task.setArrOperacion(taskUpdateDto.getArrOperacion());
            task.setDateExit(taskUpdateDto.getDateExit());
            task.setDateSign(taskUpdateDto.getDateSign());
            task.setCapex(taskUpdateDto.isCapex());
            task.setOpex(taskUpdateDto.isOpex());
        }

        Task savedTask = taskRepository.save(task);
        if (taskUpdateDto.getDocumentUpdateDtoList().size() > 0 && !taskUpdateDto.getDocumentUpdateDtoList().isEmpty()) {
            documentService.updateDocument(Long.parseLong(userId), taskUpdateDto.getDocumentUpdateDtoList(), savedTask, locale);
        }
        saveComment(savedTask.getId(), Long.parseLong(userId), task.getStatus(), taskUpdateDto.getComments(), locale);
        savedTask.setComments(commentService.findAllByTaskId(id));
        return savedTask;
    }

    @Override
    public Task assignToSupplier(Long id, AssignSupplierDto assignSupplierDto, Locale locale) throws Exception {
        Task task = findById(id, locale);
        task.setType(assignSupplierDto.getType());
        task.setSupplier(userService.findById(assignSupplierDto.getSupplierId(), locale));
        task.setReferenceId(assignSupplierDto.getReferenceId());
        task.setStatus(statusService.findByNameAndEntity(Status.ASSIGNED.name(), Entity.TASK.name()));
        task.setCapex(assignSupplierDto.isCapex());
        task.setOpex(assignSupplierDto.isOpex());
        task = taskRepository.save(task);
        task.setComments(commentService.findAllByTaskId(id));
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), ASSIGN_TO_SUPPLIER, ASSIGN_TO_SUPPLIER_TEMPLATE);
        return task;
    }

    @Override
    public Task assignToTechnicalUser(Long id, Long technicalAssigneeId, String token, Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(id, locale);
        task.setTechnicalAssignee(userService.findById(technicalAssigneeId, locale));
        task.setStatus(statusService.findByNameAndEntity(Status.ASSIGNED.name(), Entity.TASK.name()));
        task = taskRepository.save(task);
        task.setComments(commentService.findAllByTaskId(id));
        if (Objects.nonNull(task.getTechnicalAssignee())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), "", ASSIGN_TASK_TO_TECHNICAL_USER, ASSIGN_TASK_TO_TECHNICAL_USER_TEMPLATE);
        }
        return task;
    }

    public LocalDate completionDate(Long id, TaskDto taskDto, Locale locale) throws Exception {
        final String DATE_FORMAT = "yyyy/MM/dd";
        final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
        Task task = findById(id, locale);
        LocalDate currentDeliveryDate = task.getDeliveryDate();
        Integer noOfdaysCompletion = task.getNoOfDays();

        currentDeliveryDate = currentDeliveryDate.plusDays(noOfdaysCompletion);
        return currentDeliveryDate;
    }

    @Override
    public Task completeTaskBySupplier(Long supplierId, List<TaskDocumentUpdateDto> taskDocumentDtos, String token, Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        if (taskDocumentDtos.isEmpty()) {
            throw new NotAllowedException(messageSource.getMessage("file.can.not.be.empty", null, locale));
        }

        Task task = findById(taskDocumentDtos.get(0).getTaskId(), locale);
        task.setSupplier(userService.findById(supplierId, locale));
        task.setStatus(statusService.findByNameAndEntity(Status.TASK_CMP_PENDING.name(), Entity.TASK.name()));
        if (!taskDocumentDtos.isEmpty()) {
            documentService.updateDocument(Long.parseLong(userId), taskDocumentDtos, task, locale);
        }
        task = taskRepository.save(task);
        task.setComments(commentService.findAllByTaskId(task.getId()));
        if (Objects.nonNull(task.getTechnicalAssignee())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), task.getSupplier().getUserName(), COMPLETE_TASK_BY_SUPPLIER, COMPLETE_TASK_BY_SUPPLIER_TEMPLATE);
        }
        return task;
    }

    public Task findById(Long id, Locale locale) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Task task = optionalTask.orElseThrow(() -> new NotFoundException(messageSource.getMessage("task.not.found", null, locale)));
        task.setComments(commentService.findAllByTaskId(id));
        task.setInvoice(taskInvoiceService.findByTaskId(task.getId()));
        return task;
    }

    @Override
    public Task updateStatusToSubmitBudget(Long id, Locale locale) throws MessagingException {
        Task task = findById(id, locale);
        task.setStatus(statusService.findByNameAndEntity(Status.BUDGET_SUBMITTED.name(), Entity.TASK.name()));
        task = taskRepository.save(task);
        task.setComments(commentService.findAllByTaskId(id));
        if (Objects.nonNull(task.getTechnicalAssignee())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), task.getSupplier().getUserName(), SUBMIT_BUDGET, SUBMIT_BUDGET_TEMPLATE);
        }
        return task;
    }

    @Override
    public Task updateStatus(Long id, String status, Locale locale) {
        Task task = findById(id, locale);
        task.setStatus(statusService.findByNameAndEntity(status, Entity.TASK.name()));
        return taskRepository.save(task);
    }

    @Override
    public Task findByTaskId(String taskId, Locale locale) {
        Task task = taskRepository.findByTaskIdIgnoreCase(taskId);
        task.setComments(commentService.findAllByTaskId(task.getId()));
        return task;
    }

    @Override
    public DashboardDTO dashboard(Pageable pageable, DashboardFilter filter, String token, Locale locale) {
        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setMonthWiseTaskCountList(monthWiseTaskCount(filter, token, locale));
        dashboard.setMonthWiseAggregatedTotalList(monthWiseAggregatedTotal(filter, token, locale));
        dashboard.setDisaggregatedTotalCostList(disaggregatedTotalCost(filter, token, locale));
        dashboard.setAverageCost(averageCost(filter, token, locale));
        dashboard.setAverageCompletionTime(averageCompletionTime(filter, token, locale));
        dashboard.setNoOfTaskCompletedAndPending(noOfTaskCompletionAndPending(filter, token, locale));
        dashboard.setTaskPendingApproval(findAllPendingApprovalTask(pageable, filter, token, locale));
        dashboard.setTaskPendingAssignment(findAllPendingAssignmentTask(pageable, filter, token, locale));
        return dashboard;
    }

    @Override
    public List<MonthWiseTaskDTO> monthWiseTaskCount(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        StringBuilder sql = new StringBuilder("select extract(year from TASK.CREATED_DATE) as year, extract(month from TASK.CREATED_DATE) as month, COUNT(ID) as count\n" +
                " from TASK where 1 =1 \n");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) sql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) sql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            sql.append(" AND to_char(CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            sql.append(" AND to_char(CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }

        String groupBy = "  group by extract(year from TASK.CREATED_DATE), extract(month from TASK.CREATED_DATE) \n" +
                " order by year, month \n";
        List<MonthWiseTaskDTO> monthWiseTaskDTOList = jdbcTemplate.query(
                sql + groupBy,
                (rs, rowNum) ->
                        new MonthWiseTaskDTO(
                                rs.getString("year"),
                                rs.getString("month"),
                                rs.getInt("count")
                        )
        );

        return monthWiseTaskDTOList;
    }

    @Override
    public List<MonthWiseAggregatedTotalDTO> monthWiseAggregatedTotal(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        StringBuilder sql = new StringBuilder("select extract(year from TASK.CREATED_DATE) as year, extract(month from TASK.CREATED_DATE) as month, (SUM(BUDGETS.PRICE) + (SUM(BUDGETS.PRICE * (TASK.TAX_PERCENTAGE/100)))) AS totalCost\n" +
                " from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) sql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) sql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }

        String groupBy = "  group by extract(year from TASK.CREATED_DATE), extract(month from TASK.CREATED_DATE) \n" +
                " order by year, month \n";
        List<MonthWiseAggregatedTotalDTO> monthWiseAggregatedTotalDTOList = jdbcTemplate.query(
                sql + groupBy,
                (rs, rowNum) ->
                        new MonthWiseAggregatedTotalDTO(
                                rs.getString("year"),
                                rs.getString("month"),
                                rs.getInt("totalCost")
                        )
        );

        return monthWiseAggregatedTotalDTOList;
    }

    @Override
    public List<DisaggregatedTotalCostDTO> disaggregatedTotalCost(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        StringBuilder sql = new StringBuilder("select TASK.TYPE, (SUM(BUDGETS.PRICE) + (SUM(BUDGETS.PRICE * (TASK.TAX_PERCENTAGE/100)))) AS totalCost \n" +
                " from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) sql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) sql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }

        String groupBy = " group by TASK.TYPE \n order by TASK.TYPE \n";
        List<DisaggregatedTotalCostDTO> disaggregatedTotalCostDTOList = jdbcTemplate.query(
                sql + groupBy,
                (rs, rowNum) ->
                        new DisaggregatedTotalCostDTO(
                                rs.getString("type"),
                                rs.getInt("totalCost")
                        )
        );

        return disaggregatedTotalCostDTOList;
    }

    @Override
    public double averageCost(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        StringBuilder countSql = new StringBuilder("select COUNT(TASK.ID) AS total \n" +
                "from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            countSql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) countSql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) countSql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            countSql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            countSql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            countSql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }

        int count = jdbcTemplate.queryForObject(countSql.toString(), Integer.class);
        if (count == 0) return 0;

        StringBuilder sql = new StringBuilder("select COALESCE(ROUND((SUM(BUDGETS.PRICE) + (SUM(BUDGETS.PRICE * (TASK.TAX_PERCENTAGE/100))))/COUNT(TASK.ID),2),0) AS avgCost \n" +
                "from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }
        if (filter.isCapex()) sql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) sql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }
        return jdbcTemplate.queryForObject(sql.toString(), Double.class);
    }

    @Override
    public double averageCompletionTime(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        StringBuilder countSql = new StringBuilder("select COUNT(TASK.ID) AS totalTask \n" +
                "from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n" +
                " AND TASK.CREATED_DATE IS NOT NULL " +
                " AND TASK.DELIVERY_DATE IS NOT NULL ");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            countSql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            countSql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) countSql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) countSql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            countSql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            countSql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            countSql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }
        int count = jdbcTemplate.queryForObject(countSql.toString(), Integer.class);

        if (count == 0) return 0;

        StringBuilder sql = new StringBuilder("select ROUND(COALESCE(SUM(TASK.DELIVERY_DATE - TASK.CREATED_DATE),0)/COUNT(TASK.ID), 2) AS averageTime \n" +
                "from TASK " +
                " INNER JOIN BUDGETS ON (BUDGETS.REFERENCE_ID = TASK.ID) where 1 =1 \n" +
                " AND TASK.CREATED_DATE IS NOT NULL " +
                " AND TASK.DELIVERY_DATE IS NOT NULL ");
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            String ids = String.join(",", filter.getSupplierIds().toString()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.USER_ID IN(").append(ids).append(")\n");
        }
        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.USER_ID=").append(userId).append("\n ");
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND (TASK.TECHNICAL_ASSIGNEE_ID =").append(userId).append(" OR TASK.CREATED_BY_ID=").append(userId).append(")\n ");
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            sql.append(" AND TASK.CREATED_BY_ID=").append(userId).append("\n ");
        }

        if (filter.isCapex()) sql.append(" AND TASK.CAPEX = 1").append("\n ");

        if (filter.isOpex()) sql.append(" AND TASK.OPEX = 1").append("\n ");

        if (Objects.nonNull(filter.getStartDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') >='").append(filter.getStartDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getEndDate())) {
            sql.append(" AND to_char(TASK.CREATED_DATE,'yyyy-mm-dd') <='").append(filter.getEndDate()).append("'\n ");
        }
        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            String types = String.join("','", filter.getTypes()).replace("[", "").replace("]", "");
            sql.append(" AND TASK.TYPE IN ('").append(types).append("')");
        }

        return jdbcTemplate.queryForObject(sql.toString(), Double.class);
    }

    @Override
    public NoOfTaskCompletedAndPendingDTO noOfTaskCompletionAndPending(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        Long pending = statusService.findByNameAndEntity(Status.PENDING_APPROVAL.name(), Entity.TASK.name()).getId();
        Long completed = statusService.findByNameAndEntity(Status.TASK_COMPLETED.name(), Entity.TASK.name()).getId();

        QTask qTask = QTask.task;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.supplier.id.eq(Long.parseLong(userId)));
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.technicalAssignee.id.eq(Long.parseLong(userId)).or(qTask.createdBy.id.eq(Long.parseLong(userId))));
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.createdBy.id.eq(Long.parseLong(userId)));
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            booleanBuilder.and(qTask.type.in(filter.getTypes()));
        }
        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            booleanBuilder.and(qTask.supplier.id.in(filter.getSupplierIds()));
        }
        if (Objects.nonNull(filter.getStartDate())) {
            booleanBuilder.and(qTask.createdDate.goe(filter.getStartDate()));
        }
        if (Objects.nonNull(filter.getEndDate())) {
            booleanBuilder.and(qTask.deliveryDate.loe(filter.getEndDate()));
        }
        if (filter.isCapex()) booleanBuilder.and(qTask.capex.eq(true));

        if (filter.isOpex()) booleanBuilder.and(qTask.opex.eq(true));

        booleanBuilder.and(qTask.status.id.eq(pending).or(qTask.status.id.eq(completed)));

        OrderSpecifier<Long> orderSpecifier = qTask.id.desc();
        List<Task> taskList = taskRepository.findAll(booleanBuilder, orderSpecifier);

        long pendingCount = taskList.stream().filter(task -> task.getStatus().getId().equals(pending)).count();
        long completedCount = taskList.stream().filter(task -> task.getStatus().getId().equals(completed)).count();
        return new NoOfTaskCompletedAndPendingDTO(pendingCount, completedCount);
    }

    @Override
    public TaskPendingApprovalDTO findAllPendingApprovalTask(Pageable pageable, DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        Long pendingApproval = statusService.findByNameAndEntity(Status.BUDGET_SUBMITTED.name(), Entity.TASK.name()).getId();

        QTask qTask = QTask.task;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.supplier.id.eq(Long.parseLong(userId)));
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.technicalAssignee.id.eq(Long.parseLong(userId)).or(qTask.createdBy.id.eq(Long.parseLong(userId))));
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.createdBy.id.eq(Long.parseLong(userId)));
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            booleanBuilder.and(qTask.type.in(filter.getTypes()));
        }

        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            booleanBuilder.and(qTask.supplier.id.in(filter.getSupplierIds()));
        }

        if (Objects.nonNull(filter.getStartDate())) {
            booleanBuilder.and(qTask.createdDate.goe(filter.getStartDate()));
        }
        if (Objects.nonNull(filter.getEndDate())) {
            booleanBuilder.and(qTask.deliveryDate.loe(filter.getEndDate()));
        }
        if (filter.isCapex()) booleanBuilder.and(qTask.capex.eq(true));

        if (filter.isOpex()) booleanBuilder.and(qTask.opex.eq(true));

        booleanBuilder.and(qTask.status.id.eq(pendingApproval));

        OrderSpecifier<Long> orderSpecifier = qTask.id.desc();
        Page<Task> taskList = taskRepository.findAll(booleanBuilder, pageable);
        return new TaskPendingApprovalDTO(taskList);
    }

    @Override
    public TaskPendingAssignmentDTO findAllPendingAssignmentTask(Pageable pageable, DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        Long draft = statusService.findByNameAndEntity(Status.DRAFT.name(), Entity.TASK.name()).getId();
        Long assigned = statusService.findByNameAndEntity(Status.ASSIGNED.name(), Entity.TASK.name()).getId();

        QTask qTask = QTask.task;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.supplier.id.eq(Long.parseLong(userId)));
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.technicalAssignee.id.eq(Long.parseLong(userId)).or(qTask.createdBy.id.eq(Long.parseLong(userId))));
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.createdBy.id.eq(Long.parseLong(userId)));
        }

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            booleanBuilder.and(qTask.type.in(filter.getTypes()));
        }

        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            booleanBuilder.and(qTask.supplier.id.in(filter.getSupplierIds()));
        }

        if (Objects.nonNull(filter.getStartDate())) {
            booleanBuilder.and(qTask.createdDate.goe(filter.getStartDate()));
        }
        if (Objects.nonNull(filter.getEndDate())) {
            booleanBuilder.and(qTask.deliveryDate.loe(filter.getEndDate()));
        }
        if (filter.isCapex()) booleanBuilder.and(qTask.capex.eq(true));

        if (filter.isOpex()) booleanBuilder.and(qTask.opex.eq(true));

        if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.status.id.eq(draft).or(qTask.status.id.eq(assigned)));
        } else {
            booleanBuilder.and(qTask.status.id.eq(draft));
        }

        Page<Task> taskList = taskRepository.findAll(booleanBuilder, pageable);
        return new TaskPendingAssignmentDTO(taskList);
    }

    @Override
    public TaskAverageTimeDTO getAverageTime(DashboardFilter filter, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        QTask qTask = QTask.task;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.supplier.id.eq(Long.parseLong(userId)));
        } else if (AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.technicalAssignee.id.eq(Long.parseLong(userId)).or(qTask.createdBy.id.eq(Long.parseLong(userId))));
        } else if (AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER.equalsIgnoreCase(role.getLabel()) || AppConstants.ROLE_LABEL_ADMIN.equalsIgnoreCase(role.getLabel())) {
            booleanBuilder.and(qTask.createdBy.id.eq(Long.parseLong(userId)));
        }
        if (filter.isCapex()) booleanBuilder.and(qTask.capex.eq(true));

        if (filter.isOpex()) booleanBuilder.and(qTask.opex.eq(true));

        if (Objects.nonNull(filter.getTypes()) && !filter.getTypes().isEmpty()) {
            booleanBuilder.and(qTask.type.in(filter.getTypes()));
        }

        if (Objects.nonNull(filter.getSupplierIds()) && !filter.getSupplierIds().isEmpty()) {
            booleanBuilder.and(qTask.supplier.id.in(filter.getSupplierIds()));
        }

        if (Objects.nonNull(filter.getStartDate())) {
            booleanBuilder.and(qTask.createdDate.goe(filter.getStartDate()));
        }
        if (Objects.nonNull(filter.getEndDate())) {
            booleanBuilder.and(qTask.createdDate.loe(filter.getEndDate()));
        }

        OrderSpecifier<Long> orderSpecifier = qTask.id.desc();
        List<Task> taskList = taskRepository.findAll(booleanBuilder, orderSpecifier);
        TaskAverageTimeDTO taskAverageTimeDTO = new TaskAverageTimeDTO();
        List<Task> list = taskList.stream().filter(t -> Objects.nonNull(t.getCreatedDate()) && Objects.nonNull(t.getDeliveryDate())).collect(Collectors.toList());
        int dayCount = 0;
        for (int i = 0; i < list.size(); i++) {
            Period period = Period.between(list.get(i).getCreatedDate(), list.get(i).getDeliveryDate());
            int days = period.getDays();
            dayCount += days;
        }
        int averageTime = list.size() > 0 ? dayCount / list.size() : 0;
        taskAverageTimeDTO.setTotal(list.size());
        taskAverageTimeDTO.setAverageTime(averageTime);
        return taskAverageTimeDTO;
    }

    @Override
    public List<Task> findAllByLoggedInUser() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> filterTask(Pageable pageable, String token, String searchText, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        Long referenceId = (long) 0;
        if (Numeric.isNumeric(searchText)) {
            referenceId = Long.parseLong(searchText);
        }
        Page<Task> taskList = null;
        List<Task> taskLists = new ArrayList<>();
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(Long.parseLong(roleId))) {
            taskLists = taskRepository.findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotionPromotionNameIgnoreCaseContainsOrSupplierEmailIgnoreCaseContainsOrTechnicalAssigneeEmailIgnoreCaseContainsOrCreatedByEmailIgnoreCaseContainsOrSocietySocietyNameIgnoreCaseContainsOrStatusNameIgnoreCaseContainsOrGarajesDescriptionIgnoreCaseContainsOrderByCreatedDateDesc(pageable, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText, searchText);
            if (referenceId > 0) {
                taskLists.addAll(taskRepository.findAllByReferenceIdLikeOrderByCreatedDateDesc(referenceId));
            }
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
                taskLists = taskLists.stream().filter(t -> t.getSupplier().getId().equals(Long.parseLong(userId))).collect(Collectors.toList());
            } else if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER) || role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
                taskLists = taskLists.stream().filter(t -> t.getCreatedBy().getId().equals(Long.parseLong(userId))).collect(Collectors.toList());
            }
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), taskLists.size());
        taskList = new PageImpl<>(taskLists.subList(start, end), pageable, taskLists.size());
        return taskList;
    }

    @Override
    public Page<Task> findAllByLoggedInUser(Pageable pageable, Long userId, Long roleId, Locale locale) {
        Role role = roleService.findById(roleId, locale);

        UserProfile userProfile = userService.findById(userId, locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(roleId)) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
                taskList = taskRepository.findAllBySupplierIdOrderByCreatedDateDesc(pageable, userId);
            } else if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER)) {
                taskList = taskRepository.findAllByCreatedByIdOrderByCreatedDateDesc(pageable, userId);
            } else if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
                taskList = taskRepository.findAllByOrderByCreatedDateDesc(pageable);
            }
        }
        return taskList;
    }

    @Override
    public Page<Task> findAllByLoggedInUserAndRole(Pageable pageable, String type, Long id, Long roleId, Locale locale) {
        Role role = roleService.findById(roleId, locale);

        UserProfile userProfile = userService.findById(id, locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(roleId)) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_SUPPLIER)) {
                taskList = taskRepository.findAllByTypeIgnoreCaseContainsAndSupplierIdOrderByCreatedDateDesc(pageable, type, id);
            } else if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_PORTFOLIO_MANAGER)) {
                taskList = taskRepository.findAllByTypeIgnoreCaseContainsAndCreatedByIdOrderByCreatedDateDesc(pageable, type, id);
            } else if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
                taskList = taskRepository.findAllByTypeIgnoreCaseContainsOrderByCreatedDateDesc(pageable, type);
            }
        }
        return taskList;
    }

    @Override
    public Page<Task> findAllTaskAssignedToTechnicalUserByType(Pageable pageable, String type, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }

        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(Long.parseLong(roleId))) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
                taskList = taskRepository.findAllByTypeIgnoreCaseContainsAndTechnicalAssigneeIdOrderByCreatedDateDesc(pageable, type, Long.parseLong(userId));
            }
        }
        return taskList;
    }

    @Override
    public Page<Task> findAllTaskAssignedToTechnicalUser(Pageable pageable, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }

        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(Long.parseLong(roleId))) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
                taskList = taskRepository.findAllByTechnicalAssigneeIdOrderByCreatedDateDesc(pageable, Long.parseLong(userId));
            }
        }
        return taskList;
    }

    @Override
    public Page<Task> findAllTaskCreatedByTechnicalUserByType(Pageable pageable, String type, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));

        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(Long.parseLong(roleId))) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
                taskList = taskRepository.findAllByTypeIgnoreCaseContainsAndCreatedByIdOrderByCreatedDateDesc(pageable, type, Long.parseLong(userId));
            }
        }
        return taskList;
    }

    @Override
    public Page<Task> findAllTaskCreatedByTechnicalUser(Pageable pageable, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));

        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        UserProfile userProfile = userService.findById(Long.parseLong(userId), locale);
        Page<Task> taskList = null;
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(Long.parseLong(roleId))) {
            if (role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_TECHNICAL_USER)) {
                taskList = taskRepository.findAllByCreatedByIdOrderByCreatedDateDesc(pageable, Long.parseLong(userId));
            }
        }
        return taskList;
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskCompanyDto> findAllCompanyByTaskId(Long taskId, Locale locale) {
        List<TaskCompanyDto> taskCompanyDtoList = new ArrayList<>();
        Task task = findById(taskId, locale);
        UserProfile userProfile = userService.findById(task.getSupplier().getId(), locale);
        TaskCompanyDto taskCompanyDto = new TaskCompanyDto();
        taskCompanyDto.setTaskId(taskId);
        taskCompanyDto.setType(task.getType());
        taskCompanyDto.setCreateDate(task.getCreatedDate());
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompany(userProfile);
        companyDto.setNoOfMembers(userService.findSupplierMemberByCompanyId(userProfile.getId()).stream().count());
        taskCompanyDto.setCompany(companyDto);
        taskCompanyDtoList.add(taskCompanyDto);
        return taskCompanyDtoList;
    }

    @Override
    public Task saveComments(TaskCommentsDto taskCommentsDto, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Task task = findById(taskCommentsDto.getTaskId(), locale);
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        saveComment(taskCommentsDto.getTaskId(), Long.parseLong(userId), task.getStatus(), taskCommentsDto.getComment(), locale);
        task = taskRepository.save(task);
        task.setComments(commentService.findAllByTaskId(task.getId()));
        return task;
    }

    @Override
    public Task findByTypeAndReferenceId(String type, Long referenceId) {
        return taskRepository.findByTypeAndReferenceId(type, referenceId);
    }

    @Override
    public Task accept(Long taskId, Locale locale) throws MessagingException {
        Task task = findById(taskId, locale);
        task.setStatus(statusService.findByNameAndEntity(Status.ACCEPTED.name(), Entity.TASK.name()));
        task.setTaxPercentage(taxPercentage);
        task = taskRepository.save(task);
        if (Objects.nonNull(task.getTechnicalAssignee())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), task.getTechnicalAssignee().getUserName(), ACCEPT_TASK_BY_SUPPLIER, ACCEPT_TASK_BY_SUPPLIER_TEMPLATE);
        }
        task.setComments(commentService.findAllByTaskId(taskId));
        return task;
    }

    @Override
    public Task approveBudget(Long taskId, String token, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(taskId, locale);
        task.setStatus(statusService.findByNameAndEntity(Status.IN_EXECUTION.name(), Entity.TASK.name()));
        task.setApprovedDate(LocalDate.now());
        task = taskRepository.save(task);
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), APPROVE_BUDGET, APPROVE_BUDGET_TEMPLATE);
        task.setComments(commentService.findAllByTaskId(taskId));
        return task;
    }

    @Override
    public Task approveTask(Long taskId, String token, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(taskId, locale);
        task.setStatus(statusService.findByNameAndEntity(Status.TASK_CMP_APPROVED.name(), Entity.TASK.name()));
        task.setApprovedDate(LocalDate.now());
        task = taskRepository.save(task);
        invoiceService.generateInvoiceNumber(task);
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), APPROVE_TASK, APPROVE_TASK_TEMPLATE);
        task.setComments(commentService.findAllByTaskId(taskId));
        return task;
    }

    @Override
    public Task incident(TaskIncidentsDto taskIncidentsDto, Locale locale) throws MessagingException {
        Task task = findById(taskIncidentsDto.getTaskId(), locale);
        task.setStatus(statusService.findByNameAndEntity(Status.HOLD.name(), Entity.TASK.name()));
        task.setSupplier(userService.findById(taskIncidentsDto.getSupplierId(), locale));
        task.setDeliveryDate(LocalDate.parse(taskIncidentsDto.getDeliveryDate()));
        task = taskRepository.save(task);
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), HOLD_TASK, HOLD_TASK_TEMPLATE);
        sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), task.getTechnicalAssignee().getUserName(), HOLD_TASK, HOLD_TASK_TEMPLATE);
        task.setComments(commentService.findAllByTaskId(taskIncidentsDto.getTaskId()));
        return task;
    }

    @Override
    public Task rejectBudgetByTechnicalUser(TaskCommentsDto dto, String token, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(dto.getTaskId(), locale);
        task.setStatus(statusService.findByNameAndEntity(Status.BUDGET_REVIEW.name(), Entity.TASK.name()));
        saveComment(dto.getTaskId(), Long.parseLong(userId), task.getStatus(), dto.getComment(), locale);
        task = taskRepository.save(task);
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), REJECT_BUDGET_BY_TECHNICAL_USER, REJECT_BUDGET_BY_TECHNICAL_USER_TEMPLATE);
        task.setComments(commentService.findAllByTaskId(task.getId()));
        return task;
    }

    @Override
    public Task rejectTaskByTechnicalUser(TaskCommentsDto dto, String token, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_TECHNICAL_USER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(dto.getTaskId(), locale);
        task.setStatus(statusService.findByNameAndEntity(Status.TASK_CMP_REVIEW.name(), Entity.TASK.name()));
        task = taskRepository.save(task);
        saveComment(dto.getTaskId(), Long.parseLong(userId), task.getStatus(), dto.getComment(), locale);
        sendEmail(task, task.getSupplier().getEmail(), task.getSupplier().getUserName(), task.getSupplier().getUserName(), HOLD_TASK, HOLD_TASK_TEMPLATE);
        task.setComments(commentService.findAllByTaskId(task.getId()));
        return task;
    }

    @Override
    public Task rejectBySupplier(TaskCommentsDto dto, String token, Locale locale) throws MessagingException {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        Task task = findById(dto.getTaskId(), locale);
        task.setStatus(statusService.findByNameAndEntity(Status.REJECTED_BY_SUPPLIER.name(), Entity.TASK.name()));
        saveComment(dto.getTaskId(), Long.parseLong(userId), task.getStatus(), dto.getComment(), locale);
        task = taskRepository.save(task);
        if (Objects.nonNull(task.getTechnicalAssignee())) {
            sendEmail(task, task.getTechnicalAssignee().getEmail(), task.getTechnicalAssignee().getUserName(), task.getSupplier().getUserName(), REJECT_TASK_BY_SUPPLIER, REJECT_TASK_BY_SUPPLIER_TEMPLATE);
        }
        task.setComments(commentService.findAllByTaskId(task.getId()));
        return task;
    }

    @Override
    public List<Task> findAllTaskBySupplierId(Pageable pageable, String type, Long id, Long roleId, Locale locale) {
        List<String> roleNames = List.of(AppConstants.ROLE_LABEL_TECHNICAL_USER, AppConstants.ROLE_LABEL_ADMIN);
        List<Long> roleList = roleService.findAllByLabels(roleNames).stream().map(Role::getId).collect(Collectors.toList());
        UserProfile userProfile = userService.findById(id, locale);
        List<Task> taskList = new ArrayList<>();
        if (Objects.nonNull(userProfile) && Objects.nonNull(userProfile.getRole()) && userProfile.getRole().getId().equals(roleId) || roleList.contains(roleId)) {
            taskList = taskRepository.findAllByTypeIgnoreCaseContainsAndSupplierIdOrderByCreatedDateDesc(pageable, type, id).getContent();
        }
        return taskList;
    }


    private Task findTaskByReferenceIdAndType(Long referenceId, String type) {
        Task task = taskRepository.findTaskByReferenceIdAndTypeIgnoreCase(referenceId, type);
        return task;
    }

    private boolean checkReferenceIdAndType(Long id, String type, Locale locale) throws Exception {
        switch (type) {
            case "ACDC":
                acdcTaskService.findById(id, locale);
                break;
            case "GLPI":
                glpiTaskService.findById(id, locale);
                break;
            case "ASSET":
                assetService.findById(id, locale);
                break;
        }
        return true;
    }

    private int getLastDayOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private Comment saveComment(Long taskId, Long createdBy, basico.task.management.model.primary.Status status, String comments, Locale locale) {
        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setStatus(status);
        comment.setCreatedBy(userService.findById(createdBy, locale));
        comment.setComment(comments);
        return commentService.save(comment);
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
