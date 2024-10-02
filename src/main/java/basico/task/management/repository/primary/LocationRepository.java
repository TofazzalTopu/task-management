package basico.task.management.repository.primary;

import basico.task.management.model.primary.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location,String> {

    Page<Location> findAllBySocietyIdAndPromotionId(Pageable pageable, Long societyId, Long promotionId);

    List<Location> findAllBySocietyIdAndPromotionIdAndDescriptionContaining(Long societyId, Long promotionId, String name);
}
