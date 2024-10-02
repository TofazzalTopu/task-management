package basico.task.management.service.impl;

import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Location;
import basico.task.management.repository.primary.LocationRepository;
import basico.task.management.service.LocationService;
import basico.task.management.service.plataforma.PrinexDwellingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final PrinexDwellingService prinexDwellingService;
    private final MessageSource messageSource;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDaily() {
        List<Location> locations = prinexDwellingService.findAllLocation();
        for (Location locationsExt : locations) {
            Optional<Location> society = locationRepository.findById(locationsExt.getId());
            if (!society.isPresent()) {
                locationRepository.save(locationsExt);
            }
        }
    }

    @Override
    public Page<Location> findAllLocation(Long societyId, Long promotionId, Pageable pageable) {
        return locationRepository.findAllBySocietyIdAndPromotionId(pageable, societyId, promotionId);
    }

    @Override
    public List<Location> findAllLocationByName(Long societyId, Long promotionId, String name) {
        return locationRepository.findAllBySocietyIdAndPromotionIdAndDescriptionContaining(societyId, promotionId, name);
    }

    @Override
    public Optional<Location> findById(String location) {
        return locationRepository.findById(location);
    }

    @Override
    public Location findByIdNotOptional(String location, Locale locale) {
        return locationRepository.findById(location).orElseThrow(() -> new NotFoundException(messageSource.getMessage("assets.not.found", null, locale)));
    }

    @Override
    public Location save(Location locationSave) {
        return locationRepository.save(locationSave);
    }

}

