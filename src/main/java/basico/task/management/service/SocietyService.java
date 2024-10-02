package basico.task.management.service;

import basico.task.management.model.primary.Society;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface SocietyService {
  
    Page<Society> findAll(Pageable pageable);

    List<Society> findAllSocietyByName(String name);

    Society findById(Long id);

    Optional<Society> findByIdOptional(Long societyId);
}
