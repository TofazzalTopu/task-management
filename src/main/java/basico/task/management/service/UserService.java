package basico.task.management.service;

import basico.task.management.dto.userprofile.ChangePasswordDto;
import basico.task.management.dto.userprofile.ForgotPasswordDto;
import basico.task.management.dto.userprofile.UpdateSupplierInfoDto;
import basico.task.management.dto.userprofile.UserProfileDto;
import basico.task.management.model.primary.UserProfile;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserService {
    UserProfile save(UserProfileDto userProfileDto, Locale locale) throws MessagingException;
    UserProfile saveLdapUser(String userName, String password, String userGroup, Locale locale);
    UserProfile saveAdminUser(String userName, String password, Locale locale);
    UserProfile update(Long id, UserProfileDto userProfileDto, Locale locale);
    UserProfile updateSupplierInfo(Long id, UpdateSupplierInfoDto supplierInfoDto, String token, Locale locale);
    UserProfile updateLoginTime(String userName);
    Optional<UserProfile> findByUserName(String userName);
    Optional<UserProfile> findByEmail(String email);
    UserProfile findById(Long id, Locale locale);
    List<UserProfile> findByRoleName(String roleName);
    List<UserProfile> findAllRegisteredUsers(String token, Locale locale);

    List<UserProfile> findAllRegisteredSuppliers(String token, Locale locale);

    List<UserProfile> findInvitedUsers(String token, Locale locale);
    UserProfile changePassword(ChangePasswordDto changePasswordDto, Locale locale);
    UserProfile updateProfileStatus(Long userId, String statusName);
    UserProfile enableStatus(Long userId, String token, Locale locale);
    UserProfile disableStatus(Long userId, String token, Locale locale);
    UserProfile forgotPassword(ForgotPasswordDto forgotPassword, Locale locale) throws MessagingException;
    List<UserProfile> findSupplierMemberByCompanyId(Long companyId);
    List<UserProfile> findAllSupplier();
    List<UserProfile> findPendingNotRegisteredUser();
    UserProfile updateLastEmailSentDate(String email);
    List<UserProfile> findAllNotActiveUsers(String token, Locale locale);
    void delete(Long id, Locale locale);
}
