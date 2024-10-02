package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.service.SocietyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/society")
public class SocietyController {


    private final MessageSource messageSource;
    private final SocietyService societyService;

    @ApiOperation(value = "Find all Society")
    @GetMapping
    public ResponseEntity<Response> findAlSociety(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), societyService.findAll(pageable)));
    }


    @ApiOperation(value = "Find all by name")
    @GetMapping("/name")
    public ResponseEntity<Response> society(@RequestParam String name,@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), societyService.findAllSocietyByName(name)));
    }


}
