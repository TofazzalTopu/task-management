package basico.task.management.repository.primary;

import basico.task.management.model.primary.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findAllByCategoryIdOrderByIdAsc(Long categoryId);

    Optional<SubCategory> findByName(String name);

    Optional<SubCategory> findByCategoryIdAndName(Long CategoryId, String name);

    List<SubCategory> findAllByStatusOrderByIdAsc(String name);

    List<SubCategory> findAllBySupplierIdOrderByIdAsc(long parseLong);
}
