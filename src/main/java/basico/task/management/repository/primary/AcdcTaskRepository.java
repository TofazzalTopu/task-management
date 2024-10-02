package basico.task.management.repository.primary;

import basico.task.management.model.primary.AcdcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcdcTaskRepository extends JpaRepository<AcdcTask,Long> {

    Page<AcdcTask> findAllByIdIsLikeOrPromotion_PromotionNameContainsOrSociety_SocietyNameContainsOrProvinceContainsOrCityContainsOrLocation_DescriptionContains(Pageable pageable, Long id, String uppcaseFilter, String uppcaseFilter1, String uppcaseFilter2, String uppcaseFilter3, String uppcaseFilter4);

}
