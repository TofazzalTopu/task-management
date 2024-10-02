package basico.task.management.repository.plataforma;

import basico.task.management.model.plataforma.PrinexPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrinexPromotionRepository extends JpaRepository<PrinexPromotion,Long> {


}
