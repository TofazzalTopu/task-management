package basico.task.management.repository.primary;

import basico.task.management.model.primary.StorageRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRoomRepository extends JpaRepository<StorageRoom,String> {

    Page<StorageRoom> findAllBySocietyIdAndPromotionId(Pageable pageable, Long societyId, Long promotionId);

    List<StorageRoom> findAllBySocietyIdAndPromotionIdAndDescriptionContaining(Long societyId, Long promotionId, String name);
}
