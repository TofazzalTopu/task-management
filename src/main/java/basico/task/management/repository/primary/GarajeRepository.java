package basico.task.management.repository.primary;

import basico.task.management.model.primary.Garaje;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GarajeRepository extends JpaRepository<Garaje,String> {

    List<Garaje> findAllBySocietyIdAndPromotionIdAndDescriptionContaining(Long societyId, Long promotionId, String name);

    Page<Garaje> findAllBySocietyIdAndPromotionIdAndDescriptionNotNull(Pageable pageable, Long societyId, Long promotionId);
}
