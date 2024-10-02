package basico.task.management.service.impl;

import basico.task.management.model.primary.Society;
import basico.task.management.repository.primary.SocietyRepository;
import basico.task.management.service.SocietyService;
import basico.task.management.service.plataforma.PrinexSocietyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SocietyServiceImpl implements SocietyService {

    private final SocietyRepository societyRepository;
    private final PrinexSocietyService prinexSocietyService;


    @Scheduled(cron = "0 0 0 * * *")
    public void updateDaily(){
     List<Society> societies= prinexSocietyService.findAllSociety();
        for (Society societyExt:societies) {
            Optional<Society> society=societyRepository.findById(societyExt.getId());
            if(!society.isPresent()){
                societyRepository.save(societyExt);
            }
        }
    }

    @Override
    public Page<Society> findAll(Pageable pageable) {
        return societyRepository.findAllByStatus(pageable,true);
    }

    @Override
    public List<Society> findAllSocietyByName(String name) {
        return societyRepository.findBySocietyNameContainingAndStatus(name,true);
    }

    @Override
    public Society findById(Long id) {
        return societyRepository.findById(id).orElseThrow();
    }

    @Override
    public Optional<Society> findByIdOptional(Long societyId) {
        return societyRepository.findById(societyId);
    }
}
