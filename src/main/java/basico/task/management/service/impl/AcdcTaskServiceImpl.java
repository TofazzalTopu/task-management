package basico.task.management.service.impl;

import basico.task.management.enums.Entity;
import basico.task.management.enums.Status;
import basico.task.management.exception.NotFoundException;
import basico.task.management.model.primary.AcdcTask;
import basico.task.management.repository.primary.AcdcTaskRepository;
import basico.task.management.service.AcdcTaskService;
import basico.task.management.service.RestApiService;
import basico.task.management.service.StatusService;
import basico.task.management.util.Numeric;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AcdcTaskServiceImpl implements AcdcTaskService {

    private final AcdcTaskRepository acdcTaskRepository;
    private final RestApiService restApiService;
    private final MessageSource messageSource;
    private final StatusService statusService;

    @Scheduled(cron = "0 0 0 * * *")
    private void saveAcdcTask() throws JsonProcessingException, ParseException {
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.NEW.name(), Entity.TASK.name());
        acdcTaskRepository.saveAll(restApiService.listAcdcTask(status));
    }

    @Override
    public AcdcTask save(AcdcTask acdcTask) throws Exception {
        return acdcTaskRepository.save(acdcTask);
    }

    @Override
    public AcdcTask update(Long id, AcdcTask acdcTask, Locale locale) throws Exception {
        AcdcTask savedAcdcTask = findById(id, locale);
        savedAcdcTask.setArrId(acdcTask.getArrId());
        savedAcdcTask.setArrOperacion(acdcTask.getArrOperacion());
        savedAcdcTask.setCode(acdcTask.getCode());
        savedAcdcTask.setProvince(acdcTask.getProvince());
        savedAcdcTask.setCity(acdcTask.getCity());
        savedAcdcTask.setDateExit(acdcTask.getDateExit());
        savedAcdcTask.setDateSign(acdcTask.getDateSign());
        return acdcTaskRepository.save(savedAcdcTask);
    }

    @Override
    public AcdcTask updateStatus(Long id, Locale locale) throws Exception {
        AcdcTask acdcTask = findById(id, locale);
        basico.task.management.model.primary.Status status = statusService.findByNameAndEntity(Status.PROCESSED.name(), Entity.TASK.name());
        acdcTask.setStatus(status);
        return acdcTaskRepository.save(acdcTask);
    }

    @Override
    public Page<AcdcTask> filterByName(String name, Pageable pageable, Locale locale) {
        String uppcaseFilter = null;
        Long id = (long) 0;
        if (Numeric.isNumeric(name)) {
            id=Long.parseLong(name);
            uppcaseFilter=name;
        } else {
            uppcaseFilter=name.toUpperCase();
        }
        return acdcTaskRepository.findAllByIdIsLikeOrPromotion_PromotionNameContainsOrSociety_SocietyNameContainsOrProvinceContainsOrCityContainsOrLocation_DescriptionContains(pageable, id,uppcaseFilter, uppcaseFilter, uppcaseFilter, uppcaseFilter, uppcaseFilter);
    }

    @Override
    public Page<AcdcTask> findAll(Pageable pageable) throws JsonProcessingException, ParseException {
        return acdcTaskRepository.findAll(pageable);

    }

    @Override
    public AcdcTask findById(Long id, Locale locale) throws Exception {
        return acdcTaskRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("acdctask.not.found", null, locale)));
    }

    @Override
    public void delete(Long id, Locale locale) throws Exception {
        findById(id, locale);
        acdcTaskRepository.deleteById(id);
    }
}
