package basico.task.management.controller;


import basico.task.management.dto.FileDTO;
import basico.task.management.dto.Response;
import basico.task.management.dto.SubCategoryDTO;
import basico.task.management.service.SubCategoryService;
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
@RequestMapping(value = "/sub-categories")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all sub categories")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("sub.category.fetched", null, locale), subCategoryService.findAllCategoryAndSubCategories()));
    }

    @ApiOperation(value = "download file")
    @GetMapping(value = "/download/file/{id}")
    public void downloadFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable @NotNull Long id) throws Exception {
        Locale locale=new Locale("en");
        subCategoryService.downloadFile(httpServletRequest,httpServletResponse,id,locale);
    }

    @ApiOperation(value = "Get sub category by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("sub.category.fetched", null, locale), subCategoryService.findById(id, locale)));
    }

    @ApiOperation(value = "Get all sub category by category id")
    @GetMapping(value = "/category/{categoryId}")
    public ResponseEntity<Response> findByCategoryId(@PathVariable @NotNull Long categoryId,
                                                     @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("sub.category.fetched", null, locale), subCategoryService.findAllByCategoryId(categoryId)));
    }

    @ApiOperation(value = "save sub category")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @ModelAttribute SubCategoryDTO subCategory, @RequestHeader("Authorization") String token,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/sub-categories")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("sub.category.created", null, locale), subCategoryService.save(subCategory, token, locale)));
    }

    @ApiOperation(value = "save category - sub category by uploading file")
    @PostMapping("/upload")
    public ResponseEntity<Response> uploadFile(@RequestBody FileDTO file, @RequestHeader("Authorization") String token,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/sub-categories/upload")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("sub.category.created", null, locale), subCategoryService.uploadFile(file.getFile(), token, locale)));
    }

    @ApiOperation(value = "Update sub category")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @ModelAttribute SubCategoryDTO subCategory, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("sub.category.updated", null, locale), subCategoryService.update(id, subCategory, token, locale)));
    }

    @ApiOperation(value = "Delete sub category By Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        subCategoryService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }


    @ApiOperation(value = "Find all category supplier id")
    @GetMapping(value = "/supplier")
    public ResponseEntity<Response> findAllCategoryBySupplierId(@RequestHeader("Authorization") String token,
                                                                @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("sub.category.fetched", null, locale), subCategoryService.findAllSubCategoryBySupplierId(token)));
    }

    @ApiOperation(value = "Find all pending category")
    @GetMapping(value = "/pending")
    public ResponseEntity<Response> findAllPendingCategory(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("sub.category.fetched", null, locale), subCategoryService.findAllPendingSubCategory()));
    }

    @ApiOperation(value = "Update status")
    @PostMapping(value = "/{id}/update-status/{statusName}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @PathVariable @NonNull String statusName, @RequestHeader("Authorization") String token,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("sub.category.updated", null, locale), subCategoryService.updateStatus(id, statusName, token, locale)));
    }


}
