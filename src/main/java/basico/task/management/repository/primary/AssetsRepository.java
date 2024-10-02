package basico.task.management.repository.primary;

import basico.task.management.model.primary.Assets;
import basico.task.management.model.primary.Group;

import java.util.List;

import basico.task.management.projection.GarajProjection;
import basico.task.management.projection.PromotionProjection;
import basico.task.management.projection.SocietyAndLocationProjection;
import basico.task.management.projection.StorageRoomProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AssetsRepository extends JpaRepository<Assets, Long> {

    Page<Assets> findAllByTypeOrderByIdDesc(Pageable pageable, String type);

    Page<Assets> findAllByTypeInOrderByIdDesc(Pageable pageable, List<String> types);

    //not needed
    @Query(value = "select id as id,PROMOTION_NAME as promotionName from ASSETS where LOWER(PROMOTION_NAME) like %?1% and PROMOTION_NAME is not null ", nativeQuery = true)
    List<PromotionProjection> findAllByPromotionNameIsNotNullOrderByIdDesc(String name);

    //not needed
    @Query(value = "select id as id,STORAGE_ROOM as storageRoom from ASSETS where LOWER(STORAGE_ROOM) like %?1% and  STORAGE_ROOM is not null ", nativeQuery = true)
    List<StorageRoomProjection> findAllByStorageRoomIsNotNullOrderByIdDesc(String name);

    //not needed
    @Query(value = "select id as id,garaje as garaje from ASSETS where LOWER(GARAJE) like %?1% and GARAJE is not null ", nativeQuery = true)
    List<GarajProjection> findAllByGarajeIsNotNullOrderByIdDesc(String name);

    //not needed
    @Query(value = "select id as id,LOCATION as location from ASSETS where LOWER(LOCATION) like %?1% ", nativeQuery = true)
    List<SocietyAndLocationProjection> findAllByLocationName(String name);

    //not needed
    @Query(value = "select id as id,SOCIETY_ID as societyId from ASSETS where LOWER(SOCIETY_ID) like %?1% ", nativeQuery = true)
    List<SocietyAndLocationProjection> findAllSociety(String name);

    Assets findByArrId(Long arrId);

    Page<Assets> findAllByTypeInAndIdIsLikeOrProvinceIgnoreCaseContainsOrLocation_DescriptionIgnoreCaseContainsOrPromotion_PromotionNameIgnoreCaseContainsOrSociety_SocietyNameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsOrStorageRoom_DescriptionIgnoreCaseContainsOrProvinceIgnoreCaseContainsOrCommunityIgnoreCaseContainsOrMunicipalityIgnoreCaseContains(Pageable pageable, List<String> types, Long id, String searchText, String province, String searchText1, String searchText2, String searchText3, String searchText4, String searchText5, String searchText6, String searchText7);
}
