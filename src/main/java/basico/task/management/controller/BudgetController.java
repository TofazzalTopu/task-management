package basico.task.management.controller;

import basico.task.management.dto.BudgetDTO;
import basico.task.management.dto.BudgetSaveDTO;
import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.service.BudgetService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/budgets")
public class BudgetController {

    private final BudgetService budgetService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get budget by referenceId and type")
    @GetMapping(value = "/{referenceId}/{type}")
    public ResponseEntity<Response> findByReferenceIdAndType(@PathVariable @NotNull Long referenceId, @PathVariable @NotNull String type
            , @RequestHeader("Authorization") String token,
                                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("budget.fetched", null, locale), budgetService.findByReferenceIdAndType(token, referenceId, type, locale)));
    }

    @ApiOperation(value = "Save budget by referenceId and type")
    @PostMapping(value = "/{referenceId}/{type}")
    public ResponseEntity<Response> save(@PathVariable @NotNull Long referenceId, @PathVariable @NotNull String type,
                                         @RequestBody BudgetSaveDTO budgetSaveDTO, @RequestHeader("Authorization") String token,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/budgets")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("budget.fetched", null, locale), budgetService.save(token, referenceId, type, budgetSaveDTO.getBudgetDTOList(), locale)));
    }

    @ApiOperation(value = "Save budget by referenceId and type")
    @PostMapping(value = "/save/{referenceId}/{type}")
    public ResponseEntity<Response> saveBudget(@PathVariable @NotNull Long referenceId, @PathVariable @NotNull String type,
                                               @RequestBody List<BudgetDTO> budgetDTOList, @RequestHeader("Authorization") String token,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/budgets")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("budget.fetched", null, locale), budgetService.save(token, referenceId, type, budgetDTOList, locale)));
    }

    @ApiOperation(value = "Delete budget By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id, @RequestHeader("Authorization") String token,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        budgetService.delete(token, id, locale);
        return ResponseEntity.noContent().build();
    }


}
