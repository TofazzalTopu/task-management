package basico.task.management.service.plataforma.impl;

import basico.task.management.model.plataforma.PrinexGarage;
import basico.task.management.model.primary.Garaje;
import basico.task.management.repository.plataforma.PrinexGarageRepository;
import basico.task.management.service.PromotionService;
import basico.task.management.service.SocietyService;
import basico.task.management.service.plataforma.PrinexGarageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrinexGarageServiceImpl implements PrinexGarageService {


    private final PrinexGarageRepository prinexGarageRepository;
    private final SocietyService societyService;
    private final PromotionService promotionService;

    @Override
    public List<Garaje> findAllGaraje() {
        List<Garaje> garajes = new ArrayList<>();
        List<PrinexGarage> prinexGarages = prinexGarageRepository.findAll();
        for (PrinexGarage garage : prinexGarages) {
            Garaje garaje = new Garaje();
            garaje.setId(garage.getId());
            garaje.setRent(garage.getRent());
            garaje.setDescription(garage.getDescription());
            garaje.setPromotion(promotionService.findById(garage.getPromotionId()));
            garaje.setSociety(societyService.findById(garage.getSocietyId()));
            garajes.add(garaje);
        }
        return garajes;
    }
}
