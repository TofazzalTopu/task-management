package basico.task.management.service;

import basico.task.management.dto.*;
import basico.task.management.model.primary.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface TaskService {

    Task save(TaskDto taskDto, String token, Locale locale) throws Exception;

    Task update(Long id, TaskUpdateDto taskUpdateDto, String token, Locale locale) throws Exception;

    Task assignToSupplier(Long id, AssignSupplierDto assignSupplierDto, Locale locale) throws Exception;

    Task assignToTechnicalUser(Long id, Long technicalAssigneeId, String token, Locale locale) throws Exception;

    LocalDate completionDate(Long id, TaskDto taskDto, Locale locale) throws Exception;

    Task completeTaskBySupplier(Long supplierId, List<TaskDocumentUpdateDto> taskDocumentDtos, String token, Locale locale) throws Exception;

    Task findById(Long id, Locale locale);

    Task updateStatusToSubmitBudget(Long id, Locale locale) throws MessagingException;

    Task updateStatus(Long id, String status, Locale locale);

    Task findByTaskId(String taskId, Locale locale);

    DashboardDTO dashboard(Pageable pageable, DashboardFilter filter, String token, Locale locale);

    List<MonthWiseTaskDTO> monthWiseTaskCount(DashboardFilter filter, String token, Locale locale);

    List<MonthWiseAggregatedTotalDTO> monthWiseAggregatedTotal(DashboardFilter filter, String token, Locale locale);

    List<DisaggregatedTotalCostDTO> disaggregatedTotalCost(DashboardFilter filter, String token, Locale locale);

    double averageCost(DashboardFilter filter, String token, Locale locale);

    double averageCompletionTime(DashboardFilter filter, String token, Locale locale);

    NoOfTaskCompletedAndPendingDTO noOfTaskCompletionAndPending(DashboardFilter filter, String token, Locale locale);

    TaskPendingApprovalDTO findAllPendingApprovalTask(Pageable pageable, DashboardFilter filter, String token, Locale locale);

    TaskPendingAssignmentDTO findAllPendingAssignmentTask(Pageable pageable, DashboardFilter filter, String token, Locale locale);

    TaskAverageTimeDTO getAverageTime(DashboardFilter filter, String token, Locale locale);

    List<Task> findAllByLoggedInUser();

    Page<Task> filterTask(Pageable pageable, String token, String searchText, Locale locale);

    Page<Task> findAllByLoggedInUser(Pageable pageable, Long userId, Long roleId, Locale locale);

    Page<Task> findAllByLoggedInUserAndRole(Pageable pageable, String type, Long id, Long roleId, Locale locale);

    Page<Task> findAllTaskAssignedToTechnicalUserByType(Pageable pageable, String type, String token, Locale locale);

    Page<Task> findAllTaskAssignedToTechnicalUser(Pageable pageable, String token, Locale locale);

    Page<Task> findAllTaskCreatedByTechnicalUserByType(Pageable pageable, String type, String token, Locale locale);

    Page<Task> findAllTaskCreatedByTechnicalUser(Pageable pageable, String token, Locale locale);

    void delete(Long id, Locale locale);

    List<TaskCompanyDto> findAllCompanyByTaskId(Long taskId, Locale locale);

    Task saveComments(TaskCommentsDto taskCommentsDto, String token, Locale locale);

    Task findByTypeAndReferenceId(String type, Long referenceId);

    Task accept(Long taskId, Locale locale) throws MessagingException;

    Task approveBudget(Long taskId, String token, Locale locale) throws MessagingException;

    Task approveTask(Long taskId, String token, Locale locale) throws MessagingException;

    Task incident(TaskIncidentsDto taskIncidentsDto, Locale locale) throws MessagingException;

    Task rejectBudgetByTechnicalUser(TaskCommentsDto taskCommentsDto, String token, Locale locale) throws MessagingException;

    Task rejectTaskByTechnicalUser(TaskCommentsDto taskCommentsDto, String token, Locale locale) throws MessagingException;

    Task rejectBySupplier(TaskCommentsDto taskCommentsDto, String token, Locale locale) throws MessagingException;

    List<Task> findAllTaskBySupplierId(Pageable pageable, String type, Long id, Long roleId, Locale locale);

}
