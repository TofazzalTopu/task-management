package basico.task.management.service;

import basico.task.management.dto.FilterDto;
import basico.task.management.model.primary.GlpiTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Locale;

public interface GlpiTaskService {

    Page<GlpiTask> findAll(Pageable pageable, FilterDto filterDto, String token, Locale locale);

    GlpiTask findById(Long id, Locale locale) throws Exception;

    void delete(Long id, Locale locale) throws Exception;

    GlpiTask save(GlpiTask glpiTask) throws Exception;

    GlpiTask update(Long id, GlpiTask glpiTask, Locale locale) throws Exception;

    GlpiTask updateStatus(Long id, Locale locale) throws Exception;

    Page<GlpiTask> filterGlpi(Pageable pageable, String token, String toUpperCase, Locale locale);

}
