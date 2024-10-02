package basico.task.management.controller;


import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.service.RoleService;
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
@RequestMapping(value = "/roles")
public class RoleController {

    private final RoleService roleService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all role")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.role", null, locale), roleService.findAll()));
    }

    @ApiOperation(value = "Get role by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.role", null, locale), roleService.findById(id, locale)));
    }


    @ApiOperation(value = "save role")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestParam String name,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/role")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("create.role", null, locale), roleService.save(name)));
    }

    @ApiOperation(value = "Update role")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestParam String name,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.role", null, locale), roleService.update(id, name, locale)));
    }

    @ApiOperation(value = "Delete Role By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        roleService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }


}
