package basico.task.management.repository.primary;

import basico.task.management.model.primary.GlpiTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlpiTaskRepository extends JpaRepository<GlpiTask, Long> {

    Page<GlpiTask> findByUserGroupContainsOrEmailContains(Pageable pageable, String userGroup, String email);

    GlpiTask findByTicketId(Long ticketId);

    Page<GlpiTask> findAllByEmail(Pageable pageable, String email);

    Page<GlpiTask> findByUserGroupContainsAndEmail(Pageable pageable, String userGroup, String email);

    Page<GlpiTask> findAllByIdIsLikeOrTicketIdIsLikeOrLocation_DescriptionContainsOrPromotion_PromotionNameContainsOrSociety_SocietyNameContainsOrGarajes_DescriptionContainsOrStorageRoom_DescriptionContainsOrProvinceContainsOrDirectionContainsOrCityContains(Pageable pageable, Long id, Long ticketId, String searchText, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5, String searchText6, String searchText7);

//    Page<GlpiTask> findAllByEmailAndIdIsLikeOrTicketIdIsLikeOrLocationDescriptionContainsOrPromotionPromotionNameContainsOrSocietySocietyNameContainsOrGarajesDescriptionContainsOrStorageRoomDescriptionContainsOrProvinceContainsOrDirectionContainsOrCityContains(Pageable pageable, String email, Long id, Long ticketId, String searchText, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5, String searchText6, String searchText7);
    List<GlpiTask> findAllByEmailAndIdIsLikeOrTicketIdIsLikeOrLocationDescriptionContainsOrPromotionPromotionNameContainsOrSocietySocietyNameContainsOrGarajesDescriptionContainsOrStorageRoomDescriptionContainsOrProvinceContainsOrDirectionContainsOrCityContains(Pageable pageable, String email, Long id, Long ticketId, String searchText, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5, String searchText6, String searchText7);

    @Query(value = "SELECT * FROM GLPI_TASK WHERE EMAIL =?1 ORDER BY ?#{#pageable}", countQuery = "SELECT * FROM GLPI_TASK WHERE EMAIL =?1", nativeQuery = true)
    Page<GlpiTask> findByEmail(String lastname, Pageable pageable);
}
