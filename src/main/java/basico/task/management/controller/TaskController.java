package basico.task.management.controller;

import basico.task.management.dto.*;
import basico.task.management.model.primary.Task;
import basico.task.management.service.TaskService;
import basico.task.management.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tasks")
public class TaskController {

    private final TaskService taskService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all tasks")
    @GetMapping
    public ResponseEntity<Response> findAllByLoggedInUser(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                          @RequestHeader("Authorization") String token,
                                                          @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllByLoggedInUser(pageable, Long.parseLong(userId), Long.parseLong(roleId), locale)));
    }

    @ApiOperation(value = "Get all tasks by type")
    @GetMapping("/type/{typeName}")
    public ResponseEntity<Response> findAllByLoggedInUserAndRole(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @PathVariable @NotNull String typeName, @RequestHeader("Authorization") String token,
                                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllByLoggedInUserAndRole(pageable, typeName, Long.parseLong(userId), Long.parseLong(roleId), locale)));
    }

    @ApiOperation(value = "Find Task By Type And ReferenceId")
    @GetMapping("/type/{typeName}/{referenceId}")
    public ResponseEntity<Response> findByTypeAndReferenceId(@PathVariable @NotNull String typeName, @PathVariable @NotNull Long referenceId, @RequestHeader("Authorization") String token,
                                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        Task task = taskService.findByTypeAndReferenceId(typeName, referenceId);
        if (Objects.isNull(task)) {
            return new ResponseEntity<>(new Response(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("task.not.found", null, locale), new String[0]), HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                    messageSource.getMessage("task.fetch", null, locale), task));
        }
    }

    @ApiOperation(value = "Get all tasks assigned to technical user")
    @GetMapping("/assignedToTechnicalUser")
    public ResponseEntity<Response> tasksAssignedToTechnicalUser(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                                 @RequestHeader("Authorization") String token,
                                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllTaskAssignedToTechnicalUser(pageable, token, locale)));
    }

    @ApiOperation(value = "Get all tasks assigned by technical user and type")
    @GetMapping("/assignedToTechnicalUser/type/{typeName}")
    public ResponseEntity<Response> tasksAssignedByTechnicalUser(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                                 @PathVariable @NotNull String typeName, @RequestHeader("Authorization") String token,
                                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllTaskAssignedToTechnicalUserByType(pageable, typeName, token, locale)));
    }

    @ApiOperation(value = "Get all tasks created by technical user")
    @GetMapping("/createdByTechnicalUser")
    public ResponseEntity<Response> tasksCreatedByTechnicalUser(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                                @RequestHeader("Authorization") String token,
                                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllTaskCreatedByTechnicalUser(pageable, token, locale)));
    }

    @ApiOperation(value = "Get all tasks created by technical user by type")
    @GetMapping("/createdByTechnicalUser/type/{typeName}")
    public ResponseEntity<Response> tasksCreatedByTechnicalUserByType(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                                      @PathVariable @NotNull String typeName, @RequestHeader("Authorization") String token,
                                                                      @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findAllTaskCreatedByTechnicalUserByType(pageable, typeName, token, locale)));
    }

    @ApiOperation(value = "Get task by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findById(id, locale)));
    }

    @ApiOperation(value = "Get task by taskId")
    @GetMapping(value = "/taskId/{taskId}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull String taskId,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.findByTaskId(taskId, locale)));
    }

    @ApiOperation(value = "Filter task")
    @GetMapping(value = "/filter/{searchText}")
    public ResponseEntity<Response> filterTask(@RequestParam int pageNumber, @RequestParam int pageSize, @PathVariable @NotNull String searchText,
                                               @RequestHeader("Authorization") String token,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("task.fetch", null, locale), taskService.filterTask(pageable, token, searchText, locale)));
    }

    @ApiOperation(value = "Get all companies task by id")
    @GetMapping(value = "/{id}/companies")
    public ResponseEntity<Response> findAllCompanyByTaskId(@PathVariable @NotNull Long id,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("company.fetch", null, locale), taskService.findAllCompanyByTaskId(id, locale)));
    }

    @ApiOperation(value = "Create tasks")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @ModelAttribute TaskDto taskDto, @RequestHeader("Authorization") String token,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/tasks")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("task.create", null, locale), taskService.save(taskDto, token, locale)));
    }

    @ApiOperation(value = "Update tasks")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @ModelAttribute TaskUpdateDto taskUpdateDto, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(), messageSource.getMessage("task.update", null, locale), taskService.update(id, taskUpdateDto, token, locale)));
    }

    @ApiOperation(value = "Assign task to supplier")
    @PutMapping(value = "/{id}/assign")
    public ResponseEntity<Response> assignToSupplier(@PathVariable @NotNull Long id, @Valid @RequestBody AssignSupplierDto assignSupplierDto, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(), messageSource.getMessage("task.update", null, locale), taskService.assignToSupplier(id, assignSupplierDto, locale)));
    }

    @ApiOperation(value = "Assign task to technical user")
    @PutMapping(value = "/{id}/technicalUser/{technicalUserId}")
    public ResponseEntity<Response> assignToTechnicalUser(@PathVariable @NotNull Long id, @PathVariable @NotNull Long technicalUserId,
                                                          @RequestHeader("Authorization") String token,
                                                          @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.update", null, locale), taskService.assignToTechnicalUser(id, technicalUserId, token, locale)));
    }

    @ApiOperation(value = "Complete task by supplier")
    @PostMapping(value = "/complete/{supplierId}")
    public ResponseEntity<Response> completeTaskBySupplier(@PathVariable @NotNull Long supplierId, @Valid @ModelAttribute List<TaskDocumentUpdateDto> taskDocumentUpdateDtoList,
                                                           @RequestHeader("Authorization") String token,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.update", null, locale), taskService.completeTaskBySupplier(supplierId, taskDocumentUpdateDtoList, token, locale)));
    }

    @ApiOperation(value = "Delete task By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        taskService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Create comment")
    @PutMapping(value = "/comments")
    public ResponseEntity<Response> saveCommentForTask(@Valid @RequestBody TaskCommentsDto taskCommentsDto,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/comments")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("task.comments.create", null, locale), taskService.saveComments(taskCommentsDto, token, locale)));
    }

    @ApiOperation(value = "Accept task")
    @PutMapping(value = "/accept/{id}")
    public ResponseEntity<Response> accept(@PathVariable @NotNull Long id, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.accepted", null, locale), taskService.accept(id, locale)));
    }

    @ApiOperation(value = "Approve Budget")
    @PutMapping(value = "/approve/{id}")
    public ResponseEntity<Response> approveBudget(@PathVariable @NotNull Long id, @RequestHeader("Authorization") String token,
                                                  @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.accepted", null, locale), taskService.approveBudget(id, token, locale)));
    }

    @ApiOperation(value = "Approve task")
    @PutMapping(value = "/techUser/approve/{id}")
    public ResponseEntity<Response> approveTask(@PathVariable @NotNull Long id, @RequestHeader("Authorization") String token,
                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.approved", null, locale), taskService.approveTask(id, token, locale)));
    }

    @ApiOperation(value = "Incident task")
    @PutMapping(value = "/incident")
    public ResponseEntity<Response> hold(@RequestBody TaskIncidentsDto taskIncidentsDto, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.incident", null, locale), taskService.incident(taskIncidentsDto, locale)));
    }

    @ApiOperation(value = "Reject budget")
    @PutMapping(value = "/reject/budget")
    public ResponseEntity<Response> rejectBudget(@Valid @RequestBody TaskCommentsDto taskCommentsDto, @RequestHeader("Authorization") String token,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("budget.rejected", null, locale), taskService.rejectBudgetByTechnicalUser(taskCommentsDto, token, locale)));
    }

    @ApiOperation(value = "Reject task by supplier")
    @PutMapping(value = "/supplier/reject")
    public ResponseEntity<Response> supplierRejectTask(@Valid @RequestBody TaskCommentsDto taskCommentsDto, @RequestHeader("Authorization") String token,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.rejected", null, locale), taskService.rejectBySupplier(taskCommentsDto, token, locale)));
    }

    @ApiOperation(value = "Reject task by technical user")
    @PutMapping(value = "/techUser/reject")
    public ResponseEntity<Response> techUserRejectTask(@Valid @RequestBody TaskCommentsDto taskCommentsDto, @RequestHeader("Authorization") String token,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("task.rejected", null, locale), taskService.rejectTaskByTechnicalUser(taskCommentsDto, token, locale)));
    }

    @ApiOperation(value = "Get all task for by supplier id")
    @GetMapping(value = "/supplier/{id}/{type}")
    public ResponseEntity<Response> getAllTaskBySupplierId(Pageable pageable, @PathVariable @NotNull String type, @PathVariable @NotNull Long id,
                                                           @RequestHeader("Authorization") String token,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        if (Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.task.fetch", null, locale), taskService.findAllTaskBySupplierId(pageable, type, id, Long.parseLong(roleId), locale)));
    }


}
