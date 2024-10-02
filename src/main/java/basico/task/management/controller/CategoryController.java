package basico.task.management.controller;


import basico.task.management.dto.CategoryDTO;
import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
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
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {

    private final MessageSource messageSource;
    private final CategoryService categoryService;

    @ApiOperation(value = "Get all categories")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("category.fetched", null, locale), categoryService.findAll()));
    }

    @ApiOperation(value = "download file")
    @GetMapping(value = "/download/file/{id}")
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable @NotNull Long id) throws Exception {
        Locale locale=new Locale("en");
        categoryService.downloadFile(httpServletRequest,httpServletResponse,id,locale);
    }

    @ApiOperation(value = "Get category by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("category.fetched", null, locale), categoryService.findById(id, locale)));
    }

    @ApiOperation(value = "save category")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @ModelAttribute CategoryDTO categoryDTO, @RequestHeader("Authorization") String token,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/group")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("category.created", null, locale), categoryService.save(categoryDTO, token, locale)));
    }

    @ApiOperation(value = "Update category")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @ModelAttribute CategoryDTO categoryDTO, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("category.updated", null, locale), categoryService.update(id, categoryDTO, token, locale)));
    }

    @ApiOperation(value = "Update status")
    @PostMapping(value = "/{id}/update-status/{statusName}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @PathVariable @NonNull String statusName, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("category.updated", null, locale), categoryService.updateStatus(id, statusName, token, locale)));
    }

    @ApiOperation(value = "Delete category By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        categoryService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find all category supplier id")
    @GetMapping(value = "/supplier")
    public ResponseEntity<Response> findAllCategoryBySupplierId(@RequestHeader("Authorization") String token,
                                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("category.fetched", null, locale), categoryService.findAllCategoryBySupplierId(token)));
    }

    @ApiOperation(value = "Find all pending category")
    @GetMapping(value = "/pending")
    public ResponseEntity<Response> findAllPendingCategory(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("category.fetched", null, locale), categoryService.findAllPendingCategory()));
    }

}
