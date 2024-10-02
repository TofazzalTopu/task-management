package basico.task.management.service;

import basico.task.management.model.primary.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface LocationService {
    Page<Location> findAllLocation(Long societyId, Long promotionId, Pageable pageable);

    List<Location> findAllLocationByName(Long societyId, Long promotionId, String name);

    Optional<Location> findById(String location);

    Location findByIdNotOptional(String location,  Locale locale);

    Location save(Location locationSave);
}
