package basico.task.management.controller;


import basico.task.management.dto.GroupCheckListDto;
import basico.task.management.dto.Response;
import basico.task.management.service.GroupCheckListService;
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
@RequestMapping(value = "/group-checklist")
public class GroupChecklistController {

    private GroupCheckListService groupCheckListService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all group checklist")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.group.checklist", null, locale), groupCheckListService.findAll()));
    }

    @ApiOperation(value = "Get group checklist by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.group.checklist", null, locale), groupCheckListService.findById(id, locale)));
    }


    @ApiOperation(value = "create group checklist")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody GroupCheckListDto groupCheckList,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/group-checklist")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("create.group.checklist", null, locale), groupCheckListService.save(groupCheckList, locale)));
    }

    @ApiOperation(value = "Update group checklist")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody GroupCheckListDto groupCheckListDto,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.group.checklist", null, locale), groupCheckListService.update(id, groupCheckListDto, locale)));
    }

    @ApiOperation(value = "Delete Group checklist By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        groupCheckListService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

}
