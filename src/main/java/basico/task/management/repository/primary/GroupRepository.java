package basico.task.management.repository.primary;

import basico.task.management.model.primary.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByGroupName(String name);
}
