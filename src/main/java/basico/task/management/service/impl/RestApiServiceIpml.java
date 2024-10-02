package basico.task.management.service.impl;

import basico.task.management.model.primary.AcdcTask;
import basico.task.management.model.primary.Location;
import basico.task.management.model.primary.Promotion;
import basico.task.management.model.primary.Society;
import basico.task.management.service.LocationService;
import basico.task.management.service.PromotionService;
import basico.task.management.service.RestApiService;
import basico.task.management.service.SocietyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestApiServiceIpml implements RestApiService {

    @Value("${acdc.fetchurl}")
    private String acdcUrl;

    private final ObjectMapper objectMapper;
    private final SocietyService societyService;
    private final PromotionService promotionService;
    private final LocationService locationService;

    @Override
    public List<AcdcTask> listAcdcTask(basico.task.management.model.primary.Status status) throws JsonProcessingException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(acdcUrl, HttpMethod.GET, getHttpEntity(), String.class);
        List<List<String>> acdcData = objectMapper.readValue(response.getBody(), List.class);
        List<AcdcTask> acdcTasks = new LinkedList<>();
        for (List<String> data : acdcData) {
            AcdcTask acdcTask = new AcdcTask();
            acdcTask.setArrId(data.get(0));
            acdcTask.setStatus(status);
            acdcTask.setArrOperacion(data.get(3));
            acdcTask.setCode(data.get(4));
            acdcTask.setProvince(data.get(6));
            acdcTask.setCity(data.get(7));
            acdcTask.setDateExit(format.parse(data.get(9)));
            acdcTask.setDateSign(data.get(10));
            Optional<Society> society = societyService.findByIdOptional(Long.valueOf(data.get(1)));
            if (society.isPresent()) {
                acdcTask.setSociety(society.get());
                Optional<Promotion> promotion = promotionService.findByIdOptional(Long.valueOf(data.get(2)));
                Promotion promotionExt = null;
                if (promotion.isPresent()) {
                    promotionExt = promotion.get();
                    acdcTask.setPromotion(promotion.get());
                } else {
                    Promotion promotionSave = new Promotion();
                    promotionSave.setId(Long.valueOf(data.get(2)));
                    promotionSave.setSociety(society.get());
                    promotionSave.setPromotionName(data.get(5));
                    promotionExt = promotionService.save(promotionSave);
                    acdcTask.setPromotion(promotionExt);
                }
                Optional<Location> location = locationService.findById(data.get(4));
                if (location.isPresent()) {
                    acdcTask.setLocation(location.get());
                } else {
                    Location locationSave = new Location();
                    locationSave.setId(data.get(4));
                    locationSave.setPromotion(promotionExt);
                    locationSave.setSociety(society.get());
                    locationSave.setDescription(data.get(8));
                    locationService.save(locationSave);
                    acdcTask.setLocation(locationSave);
                }
                acdcTask.setSociety(society.get());
                acdcTasks.add(acdcTask);
            }
        }
        return acdcTasks;
    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

}
