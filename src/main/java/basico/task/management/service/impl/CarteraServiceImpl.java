package basico.task.management.service.impl;

import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.Cartera;
import basico.task.management.repository.primary.CarteraRepository;
import basico.task.management.service.CarteraService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CarteraServiceImpl implements CarteraService {

    private final CarteraRepository carteraRepository;
    private final MessageSource messageSource;

    @Override
    public Cartera save(Cartera cartera, Locale locale) {
        Cartera savedCartera = findByName(cartera.getName());
        if (Objects.nonNull(savedCartera))
            throw new NotFoundException(messageSource.getMessage("cartera.not.found", null, locale));
        cartera.setStartDate(new Date());
        return carteraRepository.save(cartera);
    }

    @Override
    public Cartera update(Long id, Cartera cartera, Locale locale) {
        Cartera savedCartera = findById(id, locale);
        if (!savedCartera.getName().equalsIgnoreCase(cartera.getName()))
            throw new NotFoundException(messageSource.getMessage("cartera.name.already.exist", null, locale));
        savedCartera.setName(cartera.getName());
        savedCartera.setManagementType(cartera.getManagementType());
        savedCartera.setPrinexID(cartera.getPrinexID());
        savedCartera.setCif(cartera.getCif());
        savedCartera.setCompany(cartera.getCompany());
        savedCartera.setLogo(cartera.getLogo());
        savedCartera.setAddress(cartera.getAddress());
        savedCartera.setWebsite(cartera.getWebsite());
        savedCartera.setManager(cartera.getManager());
        savedCartera.setNoOfAssets(cartera.getNoOfAssets());
        savedCartera.setStartDate(cartera.getStartDate());
        return carteraRepository.save(savedCartera);
    }

    @Override
    public Cartera findById(Long id, Locale locale) {
        return carteraRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("cartera.not.found", null, locale)));
    }

    @Override
    public Cartera findByName(String name) {
        return carteraRepository.findByName(name);
    }

    @Override
    public List<Cartera> findByNameContains(String name) {
        return carteraRepository.findByNameContains(name);
    }

    @Override
    public List<Cartera> findAll() {
        return carteraRepository.findAll();
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        carteraRepository.deleteById(id);
    }
}
