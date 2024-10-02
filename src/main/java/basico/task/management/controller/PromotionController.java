package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.dto.promotion.PromotionDto;
import basico.task.management.exception.Messages;
import basico.task.management.service.PromotionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Find all Promotion")
    @GetMapping
    public ResponseEntity<Response> findAll(@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize,
                                                  @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.promotion", null, locale), promotionService.findAll(pageable)));
    }

    @ApiOperation(value = "Find all Promotion By Society Id")
    @GetMapping(value = "/{societyId}")
    public ResponseEntity<Response> findAlSociety(@PathVariable Long societyId,@RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.promotion", null, locale), promotionService.findAllBySocietyId(societyId,pageable)));
    }


    @ApiOperation(value = "Find all by name")
    @GetMapping("/name/{societyId}")
    public ResponseEntity<Response> society(@PathVariable Long societyId,@RequestParam String name,@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.promotion", null, locale), promotionService.findAllBySocietyIdAndName(societyId,name)));
    }


}
