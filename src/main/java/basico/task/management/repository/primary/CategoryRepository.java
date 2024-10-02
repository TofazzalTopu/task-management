package basico.task.management.repository.primary;

import basico.task.management.model.primary.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    List<Category> findAllByStatusOrderByIdAsc(String status);

    List<Category> findAllBySupplierIdOrderByIdAsc(Long userId);
}
