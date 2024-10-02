package basico.task.management.controller;


import basico.task.management.dto.Response;
import basico.task.management.model.primary.Group;
import basico.task.management.service.GroupService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/groups")
public class GroupController {

    private final GroupService groupService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all group")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.group", null, locale), groupService.findAll()));
    }

    @ApiOperation(value = "Get group by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.group", null, locale), groupService.findById(id)));
    }


    @ApiOperation(value = "save group")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody Group group,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/group")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("create.group", null, locale), groupService.save(group)));
    }

    @ApiOperation(value = "Update group")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody Group group,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.group", null, locale), groupService.update(id, group)));
    }

    @ApiOperation(value = "Delete Group By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
