package basico.task.management.repository.primary;

import basico.task.management.model.primary.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByReferenceIdAndType(Long referenceId, String type);

    List<Budget> findAllByReferenceId(Long id);
}
