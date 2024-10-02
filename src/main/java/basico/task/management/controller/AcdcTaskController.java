package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.model.primary.AcdcTask;
import basico.task.management.service.AcdcTaskService;
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
import java.net.URISyntaxException;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/acdc-task")
public class AcdcTaskController {

    private final AcdcTaskService acdcTaskService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get ACDC by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.acdctask", null, locale), acdcTaskService.findById(id, locale)));
    }

    @ApiOperation(value = "Filter by different field")
    @GetMapping(value = "/filter")
    public ResponseEntity<Response> filterByName(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,@RequestParam(name = "searchText") String name,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.acdctask", null, locale), acdcTaskService.filterByName(name,pageable, locale)));
    }

    @ApiOperation(value = "Update Status to PROCESSED")
    @PutMapping(value = "/update-status/{id}")
    public ResponseEntity<Response> updateStatus(@PathVariable @NotNull Long id,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.AcdcTask", null, locale), acdcTaskService.updateStatus(id, locale)));
    }

    @ApiOperation(value = "Delete ACDC by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        acdcTaskService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find all Acdc task")
    @GetMapping
    public ResponseEntity<Response> findAllACDC(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.acdctask", null, locale), acdcTaskService.findAll(pageable)));
    }

    @ApiOperation(value = "Create Acdc task")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody AcdcTask acdcTask,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        try {
            return ResponseEntity.created(new URI("/acdcTask")).body(new Response<>(HttpStatus.CREATED.value(),
                    messageSource.getMessage("create.AcdcTask", null, locale), acdcTaskService.save(acdcTask)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "Update Acdc task")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody AcdcTask acdcTask,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.AcdcTask", null, locale), acdcTaskService.update(id, acdcTask, locale)));
    }

}
