package basico.task.management.service.impl;


import basico.task.management.model.primary.Promotion;
import basico.task.management.repository.primary.PromotionRepository;
import basico.task.management.service.PromotionService;
import basico.task.management.service.plataforma.PrinexPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PrinexPromotionService prinexPromotionService;
    private final MessageSource messageSource;


    @Scheduled(cron = "0 0 0 * * *")
    public void updateDaily(){
        List<Promotion> societies= prinexPromotionService.findAllPromotion();
        for (Promotion promotionExt:societies) {
            Optional<Promotion> society=promotionRepository.findById(promotionExt.getId());
            if(!society.isPresent()){
                promotionRepository.save(promotionExt);
            }
        }
    }


    @Override
    public Page<Promotion> findAll(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    @Override
    public Page<Promotion> findAllBySocietyId(Long societyId, Pageable pageable) {
        return promotionRepository.findAllBySocietyId(pageable,societyId);
    }

    @Override
    public List<Promotion> findAllBySocietyIdAndName(Long societyId, String name) {
        return promotionRepository.findAllBySocietyIdAndPromotionNameContaining(societyId,name);
    }

    @Override
    public Promotion findById(Long idPromotion) {
        return promotionRepository.findById(idPromotion).orElseThrow();
    }

    @Override
    public Optional<Promotion> findByIdOptional(Long id) {
        return promotionRepository.findById(id);
    }

    @Override
    public Promotion save(Promotion promotionSave) {
        return promotionRepository.save(promotionSave);
    }

}