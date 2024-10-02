package basico.task.management.service.impl;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.EmailDTO;
import basico.task.management.dto.userprofile.ChangePasswordDto;
import basico.task.management.dto.userprofile.ForgotPasswordDto;
import basico.task.management.dto.userprofile.UpdateSupplierInfoDto;
import basico.task.management.dto.userprofile.UserProfileDto;
import basico.task.management.enums.Entity;
import basico.task.management.enums.UserStatusEnum;
import basico.task.management.exception.AlreadyExistException;
import basico.task.management.exception.NotAllowedException;
import basico.task.management.exception.NotFoundException;
import basico.task.management.exception.UnAuthorizedException;
import basico.task.management.model.primary.Role;
import basico.task.management.model.primary.Status;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.repository.primary.UserProfileRepository;
import basico.task.management.service.MailSender;
import basico.task.management.service.RoleService;
import basico.task.management.service.StatusService;
import basico.task.management.service.UserService;
import basico.task.management.util.DateUtil;
import basico.task.management.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;

import static basico.task.management.constant.AppConstants.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;
    private final MessageSource messageSource;
    private final StatusService statusService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String systemEmail;

    @Value("${invite.link}")
    private String link;

    @Value("${backend.url}")
    private String backendUrl;


    public static String BASICO_DOMAIN = "@basico.es";

    /**
     * @param userProfileDto used to create the user that is suppiler
     */
    @Override
    public UserProfile save(UserProfileDto userProfileDto, Locale locale) throws MessagingException {
        UserProfile userProfile = new UserProfile();
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(userProfileDto.getUserName());

        if (optionalUserProfile.isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("user.exits", null, locale));
        }
        if (findByEmail(userProfileDto.getEmail()).isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("user.email.exits", null, locale));
        }

        if (userProfileDto.isAdmin()) {
            UserProfile profile = findByCompanyName(userProfileDto.getCompany());
            if (Objects.nonNull(profile)) {
                throw new AlreadyExistException(messageSource.getMessage("company.already.exits", null, locale));
            }
            userProfile.setCompany(userProfileDto.getCompany());
        } else {
            UserProfile profile = findByCompanyName(userProfileDto.getCompany());
            if (Objects.isNull(profile)) {
                throw new NotFoundException(messageSource.getMessage("company.not.found", null, locale));
            }
            userProfile.setCompanyId(profile.getId());
        }
        Optional<Role> optionalRole = roleService.findByName(AppConstants.ROLE_NAME_SUPPLIER);
        Role role = optionalRole.orElseGet(() -> roleService.save(AppConstants.ROLE_NAME_SUPPLIER));
        userProfile.setRole(role);
        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setUserName(userProfileDto.getUserName());
        userProfile.setEmail(userProfileDto.getEmail());
        userProfile.setLastEmailSendDate(new Date());
        userProfile.setSourceEmailId(userProfileDto.getSourceEmailId());
        userProfile.setSupplier(true);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.NOT_REGISTERED.name(), Entity.USER.name()));
        UUID uuid = UUID.randomUUID();
        userProfile.setToken(uuid.toString());
        userProfile = userProfileRepository.save(userProfile);
        sendEmail(userProfile.getEmail(), userProfile.getUserName(), userProfile.getToken(), SEND_INVITATION, SEND_INVITATION_TEMPLATE);
        return userProfile;
    }

    @Override
    public UserProfile saveLdapUser(String userName, String password, String userGroup, Locale locale) {
        UserProfile userProfile = new UserProfile();
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(userName);

        if (optionalUserProfile.isPresent()) {
            return optionalUserProfile.get();
        }
        String email = userName + BASICO_DOMAIN;
        if (findByEmail(email).isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("user.email.exits", null, locale));
        }

        Optional<Role> optionalRole = roleService.findByName(userGroup);
        Role role = optionalRole.orElseGet(() -> roleService.save(userGroup));
        userProfile.setRole(role);

        userProfile.setUserName(userName);
        userProfile.setEmail(email);
        userProfile.setSupplier(false);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.REGISTERED.name(), Entity.USER.name()));
        userProfile.setPassword(passwordEncoder.encode(password));
        userProfile.setToken(UUID.randomUUID().toString());
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile saveAdminUser(String userName, String password, Locale locale) {
        UserProfile userProfile = new UserProfile();
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(userName);

        if (optionalUserProfile.isPresent()) {
            throw new AlreadyExistException(messageSource.getMessage("user.exits", null, locale));
        }
        Optional<Role> optionalRole = roleService.findByName(AppConstants.ROLE_NAME_ADMIN);
        Role role = optionalRole.orElseGet(() -> roleService.save(AppConstants.ROLE_NAME_ADMIN));
        userProfile.setRole(role);

        String email = userName + BASICO_DOMAIN;
        userProfile.setUserName(userName);
        userProfile.setEmail(email);
        userProfile.setSupplier(false);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.REGISTERED.name(), Entity.USER.name()));
        userProfile.setPassword(passwordEncoder.encode(password));
        userProfile.setToken(UUID.randomUUID().toString());
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile update(Long id, UserProfileDto userProfileDto, Locale locale) {
        UserProfile userProfile = findById(id, locale);
        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setSourceEmailId(userProfileDto.getSourceEmailId());
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile updateSupplierInfo(Long id, UpdateSupplierInfoDto supplierInfoDto, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(role) || !AppConstants.ROLE_LABEL_SUPPLIER.equalsIgnoreCase(role.getLabel())) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        UserProfile userProfile = findById(id, locale);
        userProfile.setNif(supplierInfoDto.getNif());
        userProfile.setAddress(supplierInfoDto.getAddress());
        userProfile.setPhoneNumber(supplierInfoDto.getPhoneNumber());
        userProfile.setPosition(supplierInfoDto.getPosition());
        userProfile.setSupplierInfoIsUpdated(true);
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile updateLoginTime(String userName) {
        Optional<UserProfile> optionalUserProfile = findByUserName(userName);
        if (optionalUserProfile.isPresent()) {
            UserProfile userProfile = optionalUserProfile.get();
            userProfile.setLastLoginTime(new Date());
            return userProfileRepository.save(userProfile);
        }
        return null;
    }

    @Override
    public Optional<UserProfile> findByUserName(String userName) {
        return userProfileRepository.findByUserName(userName);
    }

    @Override
    public Optional<UserProfile> findByEmail(String email) {
        return userProfileRepository.findByEmailEqualsIgnoreCase(email);
    }

    @Override
    public UserProfile findById(Long id, Locale locale) {
        return userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("user.not.exits", null, locale)));
    }

    @Override
    public List<UserProfile> findByRoleName(String roleName) {
        return userProfileRepository.findAllByRoleName(roleName);
    }

    /**
     * @param changePasswordDto used to change user password
     */
    @Override
    public UserProfile changePassword(ChangePasswordDto changePasswordDto, Locale locale) {
        if (!changePasswordDto.getCofrimPassword().equals(changePasswordDto.getNewPassword())) {
            throw new NotFoundException(messageSource.getMessage("password.not.matched", null, locale));
        }
        UserProfile userProfile = userProfileRepository.findByToken(changePasswordDto.getToken());
        if (Objects.isNull(userProfile)) {
            throw new NotFoundException(messageSource.getMessage("token.notfound", null, locale));
        }
        userProfile.setToken(null);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.REGISTERED.name(), Entity.USER.name()));
        userProfile.setInviteDate(new Date());
        userProfile.setPassword(passwordEncoder.encode(changePasswordDto.getCofrimPassword()));
        return userProfileRepository.save(userProfile);
    }

    public void createPasswordResetTokenForUser(ForgotPasswordDto forgotPasswordDto, String token) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(forgotPasswordDto.getUserName());
        UserProfile userProfile = optionalUserProfile.get();
        userProfile.setToken(token);
        userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile forgotPassword(ForgotPasswordDto forgotPasswordDto, Locale locale) throws MessagingException {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserName(forgotPasswordDto.getUserName());
        if (optionalUserProfile.isPresent() && !optionalUserProfile.get().isSupplier()) {
            throw new NotAllowedException(messageSource.getMessage("reset.password.not.allowed", null, locale));
        }
        if (optionalUserProfile.isPresent()) {
            String token = UUID.randomUUID().toString();
            createPasswordResetTokenForUser(forgotPasswordDto, token);
            sendEmail(optionalUserProfile.get().getEmail(), optionalUserProfile.get().getUserName(), token, FORGOT_PASSWORD, FORGOT_PASSWORD_TEMPLATE);
        } else {
            throw new NotFoundException(messageSource.getMessage("user.not.found", null, locale));
        }
        return optionalUserProfile.get();
    }

    @Override
    public List<UserProfile> findSupplierMemberByCompanyId(Long companyId) {
        return userProfileRepository.findAllByCompanyId(companyId);
    }

    @Override
    public List<UserProfile> findAllSupplier() {
        return userProfileRepository.findAllBySupplierAndCompanyIdIsNull(true);
    }

    @Override
    public List<UserProfile> findPendingNotRegisteredUser() {
        Status status = statusService.findByNameAndEntity(UserStatusEnum.NOT_REGISTERED.name(), Entity.USER.name());
        return userProfileRepository.findAllBySupplierAndStatusIdAndLastEmailSendDateBeforeAndEmailCounterLessThan(true, status.getId(), DateUtil.getPreviousTimeInDate(), 3);
    }

    @Override
    public UserProfile updateLastEmailSentDate(String email) {
        Optional<UserProfile> userProfile = findByEmail(email);
        if (userProfile.isEmpty()) {
            throw new NotFoundException(messageSource.getMessage("user.not.found", null, new Locale("en")));
        }
        UserProfile user = userProfile.get();
        user.setEmailCounter(user.getEmailCounter() + 1);
        user.setLastEmailSendDate(new Date());
        return userProfileRepository.save(user);
    }

    @Override
    public List<UserProfile> findAllNotActiveUsers(String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.nonNull(roleId) || role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN) || role.getLabel().equalsIgnoreCase(ROLE_LABEL_TECHNICAL_USER)
                || role.getLabel().equalsIgnoreCase(ROLE_LABEL_SUPPLIER)) {
            return userProfileRepository.findAllByStatusName(UserStatusEnum.NOT_ACTIVE.name());
        } else {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
    }

    @Override
    public void delete(Long id, Locale locale) {
        findById(id, locale);
        userProfileRepository.deleteById(id);
    }


    /**
     * @param userId update user status to register
     */
    @Override
    public UserProfile updateProfileStatus(Long userId, String statusName) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(userId);
        if (optionalUserProfile.isPresent()) {
            UserProfile userProfile = optionalUserProfile.get();
            userProfile.setStatus(statusService.findByNameAndEntity(statusName.toUpperCase(Locale.ROOT), Entity.USER.name()));
            userProfileRepository.save(userProfile);
            return userProfile;
        }
        return null;
    }

    @Override
    public UserProfile enableStatus(Long userId, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(roleId) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        UserProfile userProfile = findById(userId, locale);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.REGISTERED.name(), Entity.USER.name()));
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile disableStatus(Long userId, String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);

        if (Objects.isNull(roleId) || !role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN)) {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
        UserProfile userProfile = findById(userId, locale);
        userProfile.setStatus(statusService.findByNameAndEntity(UserStatusEnum.NOT_ACTIVE.name(), Entity.USER.name()));
        return userProfileRepository.save(userProfile);
    }

    @Override
    public List<UserProfile> findAllRegisteredUsers(String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.nonNull(role) && role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN) || role.getLabel().equalsIgnoreCase(ROLE_LABEL_TECHNICAL_USER)) {
            return userProfileRepository.findAllByStatusName(UserStatusEnum.REGISTERED.name());
        } else {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
    }

    @Override
    public List<UserProfile> findAllRegisteredSuppliers(String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.nonNull(role)) {
            return userProfileRepository.findAllBySupplierAndStatusName(true, UserStatusEnum.REGISTERED.name());
        } else {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
    }

    @Override
    public List<UserProfile> findInvitedUsers(String token, Locale locale) {
        String roleId = String.valueOf(TokenUtil.getAllClaimsFromToken(token).get("roleId"));
        Role role = roleService.findById(Long.parseLong(roleId), locale);
        if (Objects.nonNull(role) && role.getLabel().equalsIgnoreCase(AppConstants.ROLE_LABEL_ADMIN) || role.getLabel().equalsIgnoreCase(ROLE_LABEL_TECHNICAL_USER)) {
            return userProfileRepository.findAllByStatusName(UserStatusEnum.NOT_REGISTERED.name());
        } else {
            throw new UnAuthorizedException(messageSource.getMessage("you.are.not.authorized", null, locale));
        }
    }

    private void sendInvitationMail(UserProfile userProfile, String token) throws MessagingException {
        sendEmail(userProfile.getEmail(), userProfile.getUserName(), token, SEND_INVITATION, SEND_INVITATION_TEMPLATE);
    }

    private UserProfile findByCompanyName(String name) {
        return userProfileRepository.findByCompany(name);
    }

    public void sendEmail(String email, String providerName, String url, String subject, String template) throws MessagingException {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setTo(email);
        emailDTO.setFrom(systemEmail);
        emailDTO.setSubject(subject);
        emailDTO.setTemplate(template);
        Map<String, Object> properties = new HashMap<>();
        properties.put("url", link + url);
        properties.put("providerName", providerName);
        properties.put("logo", backendUrl + "/resource/photo.png");
        properties.put("photo", backendUrl + "/resource/logo-white.png");
        emailDTO.setProperties(properties);
        mailSender.sendEmail(emailDTO);
    }

}
