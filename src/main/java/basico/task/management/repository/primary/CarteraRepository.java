package basico.task.management.repository.primary;

import basico.task.management.model.primary.Cartera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarteraRepository extends JpaRepository<Cartera, Long> {
    Cartera findByName(String name);
    List<Cartera> findByNameContains(String name);

}
