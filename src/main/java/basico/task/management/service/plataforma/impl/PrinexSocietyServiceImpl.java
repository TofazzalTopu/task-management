package basico.task.management.service.plataforma.impl;

import basico.task.management.model.plataforma.PrinexSociety;
import basico.task.management.model.primary.Society;
import basico.task.management.repository.plataforma.PrinexSocietyRepository;
import basico.task.management.service.plataforma.PrinexSocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PrinexSocietyServiceImpl implements PrinexSocietyService {

    private final PrinexSocietyRepository prinexSocietyRepository;

    @Override
    public List<Society> findAllSociety() {
       List<PrinexSociety> prinexSocieties=prinexSocietyRepository.findAll();
       List<Society> societies=new ArrayList<>();
        for (PrinexSociety prxSociety:prinexSocieties) {
            Society society=new Society();
            society.setId(prxSociety.getId());
            society.setSocietyName(prxSociety.getLiteralSociedad());
            society.setTelNumber(prxSociety.getTelefono());
            society.setEmail(prxSociety.getEmail());
            society.setDirection(prxSociety.getDireccion());
            society.setIBan(prxSociety.getIban());
            society.setCif(prxSociety.getCif());
            societies.add(society);
        }
        return societies;
    }
}
