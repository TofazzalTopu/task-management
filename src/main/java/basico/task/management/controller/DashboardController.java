package basico.task.management.controller;

import basico.task.management.dto.DashboardFilter;
import basico.task.management.dto.Response;
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
import java.util.Locale;
import java.util.Objects;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final TaskService taskService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Dashboard statistics")
    @PostMapping
    public ResponseEntity<Response> dashboard(@Valid @RequestBody DashboardFilter dashboardFilter, @RequestParam int pageNumber,
                                              @RequestParam int pageSize, @RequestHeader("Authorization") String token,
                                              @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.dashboard(pageable, dashboardFilter, token, locale)));
    }

    @ApiOperation(value = "Month Wise Task Count")
    @PostMapping("/monthWiseTaskCount")
    public ResponseEntity<Response> monthWiseTaskCount(@Valid @RequestBody DashboardFilter dashboardFilter,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.monthWiseTaskCount(dashboardFilter, token, locale)));
    }

    @ApiOperation(value = "Month Wise Aggregated Total")
    @PostMapping("/monthWiseAggregatedTotalCost")
    public ResponseEntity<Response> monthWiseAggregatedTotal(@Valid @RequestBody DashboardFilter filter, @RequestHeader("Authorization") String token,
                                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.monthWiseAggregatedTotal(filter, token, locale)));
    }

    @ApiOperation(value = "Month Wise Disaggregated Total Cost")
    @PostMapping("/disaggregatedTotalCost")
    public ResponseEntity<Response> disaggregatedTotalCost(@Valid @RequestBody DashboardFilter filter, @RequestHeader("Authorization") String token,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.disaggregatedTotalCost(filter, token, locale)));
    }

    @ApiOperation(value = "Average Cost")
    @PostMapping("/averageCost")
    public ResponseEntity<Response> averageCost(@Valid @RequestBody DashboardFilter filter, @RequestHeader("Authorization") String token,
                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.averageCost(filter, token, locale)));
    }

    @ApiOperation(value = "Average Completion Time")
    @PostMapping("/averageCompletionTime")
    public ResponseEntity<Response> averageCompletionTime(@Valid @RequestBody DashboardFilter filter, @RequestHeader("Authorization") String token,
                                                          @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.averageCompletionTime(filter, token, locale)));
    }

    @ApiOperation(value = "No Of Task Completion And Pending")
    @PostMapping("/noOfTaskCompletionAndPending")
    public ResponseEntity<Response> noOfTaskCompletionAndPending(@Valid @RequestBody DashboardFilter filter, @RequestHeader("Authorization") String token,
                                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.noOfTaskCompletionAndPending(filter, token, locale)));
    }

    @ApiOperation(value = "Find All Pending Approval Task")
    @PostMapping("/findAllPendingApprovalTask")
    public ResponseEntity<Response> findAllPendingApprovalTask(@Valid @RequestBody DashboardFilter filter, @RequestParam int pageNumber, @RequestParam int pageSize,
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
                messageSource.getMessage("populate.dashboard", null, locale), taskService.findAllPendingApprovalTask(pageable, filter, token, locale)));
    }

    @ApiOperation(value = "Find All Pending Assignment Task")
    @PostMapping("/findAllPendingAssignmentTask")
    public ResponseEntity<Response> findAllPendingAssignmentTask(@Valid @RequestBody DashboardFilter filter, @RequestParam int pageNumber,
                                                                 @RequestParam int pageSize, @RequestHeader("Authorization") String token,
                                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("populate.dashboard", null, locale), taskService.findAllPendingAssignmentTask(pageable, filter, token, locale)));
    }

}
