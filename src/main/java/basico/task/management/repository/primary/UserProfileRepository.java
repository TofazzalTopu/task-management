package basico.task.management.repository.primary;

import basico.task.management.model.primary.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    List<UserProfile> findAllByStatusName(String name);
    List<UserProfile> findAllBySupplierAndStatusName(boolean supplier, String name);
    List<UserProfile> findByInviteDateIsNull();
    List<UserProfile> findByInviteDateIsNotNull();
    Optional<UserProfile> findByUserName(String userName);
    Optional<UserProfile> findByEmailEqualsIgnoreCase(String email);
    Optional<UserProfile> findByUserNameAndSupplier(String userName, boolean supplier);
    UserProfile findByToken(String token);

    @Query(value = "select * from users where COMPANY_NAME=:company and rownum=1",nativeQuery = true)
    UserProfile findByCompany(@Param("company") String company);

    List<UserProfile> findAllByRoleName(String roleName);
    List<UserProfile> findAllByCompanyId(Long companyId);
    List<UserProfile> findAllBySupplierAndCompanyIdIsNull(boolean supplier);
    List<UserProfile> findAllBySupplierAndStatusIdAndLastEmailSendDateBeforeAndEmailCounterLessThan(boolean b, Long statusId, Date previousTimeInDate,int value);

}
