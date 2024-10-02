package basico.task.management.controller;

import basico.task.management.dto.FilterDto;
import basico.task.management.dto.Response;
import basico.task.management.model.primary.GlpiTask;
import basico.task.management.service.GlpiTaskService;
import basico.task.management.service.glpi.GlpiService;
import basico.task.management.util.MediatypeUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/glpi-task")
public class GlpiTaskController {

    private final GlpiTaskService giplTaskService;
    private final MessageSource messageSource;
    private final GlpiService glpiService;
    private final ServletContext servletContext;

    @ApiOperation(value = "Get GlPi by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.glpitask", null, locale), giplTaskService.findById(id, locale)));
    }


    @GetMapping(value = "/get-all/file/{ticketId}")
    public ResponseEntity<Response> listFile(@PathVariable Long ticketId,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.glpitask", null, locale), glpiService.getAllGlpiFile(ticketId)));
    }

    @GetMapping(value = "/download/file/{ticketId}/{fileId}/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer ticketId,@PathVariable String fileId,@PathVariable String filename,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        InputStream in = glpiService.getGLPIFiles(ticketId, fileId);
        if (Objects.nonNull(in)) {
            MediaType mediaType = MediatypeUtils.getMediaTypeForFileName(this.servletContext, filename);
            InputStreamResource resource = new InputStreamResource(in);
            return ResponseEntity.ok()
                    // Content-Disposition
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + filename)
                    // Content-Type
                    .contentType(mediaType)
                    // Contet-Length
                    .body(resource);
        } else {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.CONFLICT.value(),
                    messageSource.getMessage("fetch.glpitask", null, locale), null));
        }


    }

    @ApiOperation(value = "Filter glpi")
    @GetMapping(value = "/filter")
    public ResponseEntity<Response> filter(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                            @RequestParam String searchText, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), giplTaskService.filterGlpi(pageable, token, searchText.toUpperCase(), locale)));

    }

    @ApiOperation(value = "Update GLPI Status")
    @PutMapping(value = "/update-status/{id}")
    public ResponseEntity<Response> updateStatus(@PathVariable @NotNull Long id,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.GlpiTask", null, locale), giplTaskService.updateStatus(id, locale)));
    }

    @ApiOperation(value = "Delete GLPI by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        giplTaskService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find all GLPI task")
    @GetMapping
    public ResponseEntity<Response> findAllGiplTask(FilterDto filterDto, @RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                    @RequestHeader(name = "Accept-Language", required = false) Locale locale, @RequestHeader("Authorization") String token) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.glpitask", null, locale), giplTaskService.findAll(pageable, filterDto, token, locale)));
    }

    @ApiOperation(value = "Create GLPI task")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody GlpiTask glpiTask,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        try {
            return ResponseEntity.created(new URI("/glpiTask")).body(new Response<>(HttpStatus.CREATED.value(),
                    messageSource.getMessage("create.GlpiTask", null, locale), giplTaskService.save(glpiTask)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "Update GLPI task")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody GlpiTask glpiTask,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.GlpiTask", null, locale), giplTaskService.update(id, glpiTask, locale)));
    }

}
