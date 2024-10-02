package basico.task.management.service;

import basico.task.management.model.primary.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PromotionService {

    Page<Promotion> findAll(Pageable pageable);

    Page<Promotion> findAllBySocietyId(Long societyId, Pageable pageable);

    List<Promotion> findAllBySocietyIdAndName(Long societyId, String name);

    Promotion findById(Long idPromotion);

    Optional<Promotion> findByIdOptional(Long valueOf);

    Promotion save(Promotion promotionSave);
}
