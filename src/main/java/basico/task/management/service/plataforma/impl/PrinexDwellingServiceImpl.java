package basico.task.management.service.plataforma.impl;

import basico.task.management.model.plataforma.PrinexDwelling;
import basico.task.management.model.primary.Location;
import basico.task.management.repository.plataforma.PrinexDwellingRepository;
import basico.task.management.service.PromotionService;
import basico.task.management.service.SocietyService;
import basico.task.management.service.plataforma.PrinexDwellingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PrinexDwellingServiceImpl implements PrinexDwellingService {


    private final PrinexDwellingRepository prinexDwellingRepository;
    private final SocietyService societyService;
    private final PromotionService promotionService;


    @Override
    public List<Location> findAllLocation() {
        List<Location> locations = new ArrayList<>();
        List<PrinexDwelling> prinexDwellings = prinexDwellingRepository.findAll();
        for (PrinexDwelling locationDb : prinexDwellings) {
            Location location = new Location();
            location.setId(locationDb.getId());
            location.setRent(locationDb.getRent());
            location.setDescription(locationDb.getDescription());
            if (Objects.nonNull(locationDb.getPromotionId())) {
              location.setPromotion(promotionService.findById(locationDb.getPromotionId().longValue()));
            }
            if (Objects.nonNull(locationDb.getSocietyId())) {
                location.setSociety(societyService.findById(locationDb.getSocietyId().longValue()));
            }
            locations.add(location);

        }
        return locations;

    }

}
