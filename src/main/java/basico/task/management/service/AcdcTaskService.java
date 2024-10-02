package basico.task.management.service;

import basico.task.management.model.primary.AcdcTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Locale;

public interface AcdcTaskService {

    Page<AcdcTask> findAll(Pageable pageable) throws JsonProcessingException, ParseException;
    AcdcTask findById(Long id, Locale locale) throws Exception;
    void delete(Long id, Locale locale) throws Exception;
	AcdcTask save(AcdcTask acdcTask) throws Exception;
	AcdcTask update(Long id, AcdcTask acdcTask, Locale locale) throws Exception;
    AcdcTask updateStatus(Long id, Locale locale) throws Exception;
    Page<AcdcTask> filterByName(String name, Pageable pageable, Locale locale);
}
