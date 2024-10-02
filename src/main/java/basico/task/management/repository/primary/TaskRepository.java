package basico.task.management.repository.primary;

import basico.task.management.model.primary.Task;
import basico.task.management.projection.TaskResponseSupplier;
import basico.task.management.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long> {

    Page<Task> findAllByTypeIgnoreCaseContainsAndSupplierIdOrderByCreatedDateDesc(Pageable pageable, String type, Long supplierId);

    Page<Task> findAllBySupplierIdOrderByCreatedDateDesc(Pageable pageable, Long supplierId);

    Page<Task> findAllByTypeIgnoreCaseContainsAndTechnicalAssigneeIdOrderByCreatedDateDesc(Pageable pageable, String type, Long technicalAssigneeId);

    Page<Task> findAllByTechnicalAssigneeIdOrderByCreatedDateDesc(Pageable pageable, Long technicalAssigneeId);

    Page<Task> findAllByTypeIgnoreCaseContainsAndCreatedByIdOrderByCreatedDateDesc(Pageable pageable, String type, Long createdById);

    Page<Task> findAllByCreatedByIdOrderByCreatedDateDesc(Pageable pageable, Long createdById);

    Page<Task> findAllByTypeIgnoreCaseContainsOrderByCreatedDateDesc(Pageable pageable, String type);

    Page<Task> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Task findTaskByReferenceIdAndTypeIgnoreCase(Long refrenceId, String type);

    Task findByTaskIdIgnoreCase(String taskId);

    @Query(value = "SELECT coalesce(max(id), 0) FROM Task")
    long findMaxId();

    Task findByTypeAndReferenceId(String type, Long referenceId);

    List<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotionPromotionNameIgnoreCaseContainsOrSupplierEmailIgnoreCaseContainsOrTechnicalAssigneeEmailIgnoreCaseContainsOrCreatedByEmailIgnoreCaseContainsOrSocietySocietyNameIgnoreCaseContainsOrStatusNameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsOrReferenceIdIsLikeOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje, Long referenceId);
    Page<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotion_PromotionNameIgnoreCaseContainsOrSupplier_EmailIgnoreCaseContainsOrTechnicalAssignee_emailIgnoreCaseContainsOrCreatedBy_emailIgnoreCaseContainsOrSociety_SocietyNameIgnoreCaseContainsOrStatus_NameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsAndSupplierIdOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje, Long supplierId);

    Page<Task> findAllByStatusId(Pageable pageable, Long statusId);
    List<Task> findAllByReferenceIdLikeOrderByCreatedDateDesc(Long referenceId);
    List<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotionPromotionNameIgnoreCaseContainsOrSupplierEmailIgnoreCaseContainsOrTechnicalAssigneeEmailIgnoreCaseContainsOrCreatedByEmailIgnoreCaseContainsOrSocietySocietyNameIgnoreCaseContainsOrStatusNameIgnoreCaseContainsOrGarajesDescriptionIgnoreCaseContainsOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje);
    Page<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotion_PromotionNameIgnoreCaseContainsOrSupplier_EmailIgnoreCaseContainsOrTechnicalAssignee_emailIgnoreCaseContainsOrCreatedBy_emailIgnoreCaseContainsOrSociety_SocietyNameIgnoreCaseContainsOrStatus_NameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsAndCreatedByIdOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje, Long userId);

//    List<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotionPromotionNameIgnoreCaseContainsOrSupplierEmailIgnoreCaseContainsOrTechnicalAssigneeEmailIgnoreCaseContainsOrCreatedByEmailIgnoreCaseContainsOrSocietySocietyNameIgnoreCaseContainsOrStatusNameIgnoreCaseContainsOrGarajesDescriptionIgnoreCaseContainsOrReferenceIdIsLikeOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje, Long referenceId);
    Page<Task> findAllByTaskIdIgnoreCaseContainsOrTypeIgnoreCaseContainsOrLocationIgnoreCaseContainsOrPromotion_PromotionNameIgnoreCaseContainsOrSupplier_EmailIgnoreCaseContainsOrTechnicalAssignee_emailIgnoreCaseContainsOrCreatedBy_emailIgnoreCaseContainsOrSociety_SocietyNameIgnoreCaseContainsOrStatus_NameIgnoreCaseContainsOrGarajes_DescriptionIgnoreCaseContainsOrReferenceIdIsLikeOrderByCreatedDateDesc(Pageable pageable, String taskId, String type, String location, String promotionName, String supplierEmail, String techUserEmail, String createdByEmail, String societyName, String statusName, String garaje, Long referenceId);

    /**
     * @param id
     * @param type
     * @return returns list of acdc task for the supplier
     */
    @Query(value = "select  t.ID,t.TYPE ,t.STATUS,t.TYPE as taskType,t.ASSIGN_DATE as assignDate,t.CREATED_DATE as createdDate,t.DELIVERY_DATE as deliveryDate,t.COMMENTS,\n" +
            "     u.COMPANY_NAME as supplierName,U.USER_ID as supplierId ,\n" +
            "    A2.ID as referenceId,A2.SOCIETY_ID as societyId\n" +
            "    ,a2.CODE as storage,a2.DIRECTION,a2.PROVINCE,a2.PROMOTION_NAME as promotionName,A2.CITY\n" +
            "    from TASK t\n" +
            "    inner join ACDC_TASK A2 on t.REFERENCE_ID=A2.ID\n" +
            "    left join USERS U on t.USER_ID = U.USER_ID\n" +
            "  where  t.USER_ID=?1 and t.TYPE=?2", nativeQuery = true)
    List<TaskResponseSupplier> findAllAcdcTaskOfSupplier(Long id, String type);

}

