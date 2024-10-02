package basico.task.management.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import basico.task.management.dto.NewOrderAssetsDto;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.model.primary.AcdcTask;
import basico.task.management.model.primary.Assets;
import basico.task.management.service.AssetsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/assets")
public class AssetsController {

    private final AssetsService assetsService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get Assets by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findById(id, locale)));
    }

    @ApiOperation(value = "Delete Assets by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        assetsService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find all Assets")
    @GetMapping
    public ResponseEntity<Response> findAllAssets(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAll(pageable)));
    }

    @ApiOperation(value = "Find New Orders By type: Precarious/Available")
    @GetMapping(value = "/newOrders")
    public ResponseEntity<Response> findNewOrders(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @RequestParam List<String> types,
                                                  @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllByTypes(pageable, types)));
    }
    @ApiOperation(value = "Filter New Orders By type: Precarious/Available")
    @GetMapping(value = "/filter")
    public ResponseEntity<Response> filter(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                           @RequestParam List<String> types, @RequestParam String searchText,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.filterNewOrders(pageable, types, searchText)));
    }

    @ApiOperation(value = "Find by type Status: Precarious/Squatted")
    @GetMapping(value = "/findtype/{type}")
    public ResponseEntity<Response> findByType(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @PathVariable String type,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllByType(pageable, type)));
    }

    @ApiOperation(value = "Add Assets")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody NewOrderAssetsDto assets,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/assets")).body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("create.assets", null, locale), assetsService.save(assets, locale)));
    }

    @ApiOperation(value = "Update Assets")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody Assets assets,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.assets", null, locale), assetsService.update(id, assets, locale)));
    }

    @ApiOperation(value = "Find all promotion")
    @GetMapping(value = "/promotion")
    public ResponseEntity<Response> promotion(@RequestParam String name, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllPromotion(name)));
    }

    @ApiOperation(value = "Find all location")
    @GetMapping(value = "/location")
    public ResponseEntity<Response> location(@RequestParam String name, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllLocation(name)));
    }

    @ApiOperation(value = "Find all society")
    @GetMapping(value = "/society")
    public ResponseEntity<Response> society(@RequestParam String name, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllSociety(name)));
    }

    @ApiOperation(value = "Find all storage")
    @GetMapping(value = "/storage")
    public ResponseEntity<Response> storage(@RequestParam String name, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllStorage(name)));
    }

    @ApiOperation(value = "Find all garaje")
    @GetMapping(value = "/garaje")
    public ResponseEntity<Response> garaje(@RequestParam String name, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), assetsService.findAllGaraje(name)));
    }

    @ApiOperation(value = "Update Status to PROCESSED")
    @PutMapping(value = "/update-status/{id}")
    public ResponseEntity<Response> updateStatus(@PathVariable @NotNull Long id,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.assets", null, locale), assetsService.updateStatus(id, locale)));
    }



}
