package basico.task.management.service.impl;

import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Garaje;
import basico.task.management.repository.primary.GarajeRepository;
import basico.task.management.service.GarajeService;
import basico.task.management.service.plataforma.PrinexGarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GarajeServiceImpl implements GarajeService {

    private final GarajeRepository garajeRepository;
    private final PrinexGarageService prinexGarageService;
    private final MessageSource messageSource;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateDaily(){
        List<Garaje> garajes= prinexGarageService.findAllGaraje();
        for (Garaje garajesExt:garajes) {
            Optional<Garaje> society=garajeRepository.findById(garajesExt.getId());
            if(!society.isPresent()){
                garajeRepository.save(garajesExt);
            }
        }
    }

    @Override
    public Page<Garaje> findAllGaraje(Long societyId, Long promotionId, Pageable pageable) {
        return garajeRepository.findAllBySocietyIdAndPromotionIdAndDescriptionNotNull(pageable,societyId,promotionId);
    }

    @Override
    public List<Garaje> findAllGarajeByName(Long societyId, Long promotionId, String name) {
        return garajeRepository.findAllBySocietyIdAndPromotionIdAndDescriptionContaining(societyId,promotionId,name);
    }

    @Override
    public Garaje findById(String garaje, Locale locale) {
        return garajeRepository.findById(garaje).orElseThrow(() -> new NotFoundException(messageSource.getMessage("assets.not.found", null, locale)));
    }

    @Override
    public Optional<Garaje> findByIdOptional(String code) {
        return garajeRepository.findById(code);
    }

    @Override
    public Garaje save(Garaje garajeSave) {
        return garajeRepository.save(garajeSave);
    }
}
