package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.service.GarajeService;
import basico.task.management.service.LocationService;
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
@RequestMapping(value = "/location")
public class LocationController {

    private final LocationService locationService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Find all location")
    @GetMapping("/society/{societyId}/promotion/{promotionId}")
    public ResponseEntity<Response> findAlGaraje(@PathVariable Long societyId, @PathVariable Long promotionId, @RequestParam(name = "pageNumber") int pageNumber, @RequestParam(name = "pageSize") int pageSize, @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.assets", null, locale), locationService.findAllLocation(societyId,promotionId,pageable)));
    }


    @ApiOperation(value = "Find all by name")
    @GetMapping("/name/society/{societyId}/promotion/{promotionId}")
    public ResponseEntity<Response> garaje(@PathVariable Long societyId,@PathVariable Long promotionId,@RequestParam String name,@RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("fetch.assets", null, locale), locationService.findAllLocationByName(societyId,promotionId,name)));
    }

}
