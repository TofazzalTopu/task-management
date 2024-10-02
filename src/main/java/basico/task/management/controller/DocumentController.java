package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.dto.TaskDocumentDto;
import basico.task.management.dto.TaskDocumentUpdateDto;
import basico.task.management.exception.NotAllowedException;
import basico.task.management.service.DocumentService;
import basico.task.management.service.TaskService;
import basico.task.management.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final TaskService taskService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get document by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("document.fetched", null, locale), documentService.findById(id, locale)));
    }


    @ApiOperation(value = "Get document by task id")
    @GetMapping(value = "/task/{taskId}")
    public ResponseEntity<Response> findByTaskId(@PathVariable @NotNull Long taskId,

                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("document.fetched", null, locale), documentService.findByTaskId(taskId)));
    }

    @ApiOperation(value = "Download file")
    @GetMapping(value = "/download/file/{taskId}/{fileId}")
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable @NotNull Long taskId, @PathVariable @NotNull String fileId) throws Exception {
        documentService.downloadFile(httpServletRequest, httpServletResponse, taskId, fileId);
    }


    @ApiOperation(value = "Save document")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @ModelAttribute List<TaskDocumentDto> taskDocumentDtoList, @RequestHeader("Authorization") String token,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }
        return ResponseEntity.created(new URI("/documents")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("document.created", null, locale), documentService.saveAll(Long.parseLong(userId), taskDocumentDtoList, taskService.findById(taskDocumentDtoList.get(0).getTaskId(), locale), locale)));
    }

    @ApiOperation(value = "Update document")
    @PutMapping
    public ResponseEntity<Response> update(@Valid @ModelAttribute List<TaskDocumentUpdateDto> documentUpdateDtoList, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        String userId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("userId"));
        if (Objects.isNull(userId) || Objects.isNull(roleId)) {
            return ResponseEntity.badRequest().body(new Response<>(HttpStatus.UNAUTHORIZED.value(),
                    messageSource.getMessage("you.are.not.authorized", null, locale), null));
        }

        if (documentUpdateDtoList.isEmpty()) {
            throw new NotAllowedException(messageSource.getMessage("file.can.not.be.empty", null, locale));
        }

        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("document.update", null, locale), documentService.updateDocument(Long.parseLong(userId), documentUpdateDtoList, taskService.findById(documentUpdateDtoList.get(0).getTaskId(), locale), locale)));
    }


    @ApiOperation(value = "Delete document By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        documentService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

}
