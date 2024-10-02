package basico.task.management.repository.primary;

import basico.task.management.model.primary.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>{

    Page<Promotion> findAllBySocietyId(Pageable pageable, Long societyId);

    List<Promotion> findAllBySocietyIdAndPromotionNameContaining(Long societyId, String name);
}
