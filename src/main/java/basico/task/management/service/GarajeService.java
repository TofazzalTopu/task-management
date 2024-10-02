package basico.task.management.service;

import basico.task.management.model.primary.Garaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface GarajeService {

    Page<Garaje> findAllGaraje(Long societyId, Long promotionId, Pageable pageable);

    List<Garaje> findAllGarajeByName(Long societyId, Long promotionId, String name);

    Garaje findById(String garaje, Locale locale);

    Optional<Garaje> findByIdOptional(String code);

    Garaje save(Garaje garajeSave);
}
