package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.model.primary.Cartera;
import basico.task.management.service.CarteraService;
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
@RequestMapping(value = "/cartera")
public class CarteraController {

    private final CarteraService carteraService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Get all cartera")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.cartera", null, locale), carteraService.findAll()));
    }

    @ApiOperation(value = "Get cartera by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.cartera", null, locale), carteraService.findById(id, locale)));
    }

    @ApiOperation(value = "Get cartera by name")
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<Response> findByName(String name,
                                               @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.cartera", null, locale), carteraService.findByNameContains(name)));
    }

    @ApiOperation(value = "Create cartera")
    @PostMapping
    public ResponseEntity<Response> save(@Valid @RequestBody Cartera cartera,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/cartera")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("create.cartera", null, locale), carteraService.save(cartera, locale)));
    }

    @ApiOperation(value = "Update cartera")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody Cartera cartera,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("update.cartera", null, locale), carteraService.update(id, cartera, locale)));
    }

    @ApiOperation(value = "Get cartera by id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        carteraService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }

}
