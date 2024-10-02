package basico.task.management.service.plataforma.impl;


import basico.task.management.model.plataforma.PrinexPromotion;
import basico.task.management.model.primary.Promotion;
import basico.task.management.repository.plataforma.PrinexPromotionRepository;
import basico.task.management.service.SocietyService;
import basico.task.management.service.plataforma.PrinexPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PrinexPromotionServiceImpl implements PrinexPromotionService {

    private final PrinexPromotionRepository prinexPromotionRepository;
    private final SocietyService societyService;


    @Override
    public List<Promotion> findAllPromotion() {
        List<PrinexPromotion> prinexPromotions = prinexPromotionRepository.findAll();
        List<Promotion> promotionsList = new ArrayList<>();
        for (PrinexPromotion prxPromotion : prinexPromotions) {
            Promotion promotion = new Promotion();
            promotion.setId(prxPromotion.getId());
            promotion.setComercial(prxPromotion.getComercial());
            promotion.setPromotionName(prxPromotion.getSppromd());
            promotion.setSociety(societyService.findById(prxPromotion.getSpsocic()));
            promotionsList.add(promotion);
        }
        return promotionsList;

    }
}
