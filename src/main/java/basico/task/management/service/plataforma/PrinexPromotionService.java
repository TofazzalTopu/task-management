package basico.task.management.service.plataforma;

import basico.task.management.model.primary.Promotion;
import basico.task.management.model.primary.Society;

import java.util.List;

public interface PrinexPromotionService {

    List<Promotion> findAllPromotion();
}
