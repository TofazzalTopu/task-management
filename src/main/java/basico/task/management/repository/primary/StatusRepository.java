package basico.task.management.repository.primary;

import basico.task.management.model.primary.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status,Long> {

    Status findByNameAndEntity(String name, String entity);
    List<Status> findAllByNameInAndEntity(List<String> names, String entity);
}
