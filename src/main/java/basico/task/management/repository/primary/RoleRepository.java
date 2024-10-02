package basico.task.management.repository.primary;

import basico.task.management.model.primary.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    List<Role> findAllByNameIn(List<String> names);
    List<Role> findAllByLabelIn(List<String> labels);

}
