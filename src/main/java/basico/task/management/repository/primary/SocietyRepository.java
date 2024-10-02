package basico.task.management.repository.primary;

import basico.task.management.model.primary.Society;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocietyRepository extends JpaRepository<Society,Long> {

    Page<Society> findAllByStatus(Pageable pageable, boolean b);

    List<Society> findBySocietyNameContainingAndStatus(String name, boolean b);
}
