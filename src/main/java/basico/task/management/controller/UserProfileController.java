package basico.task.management.controller;

import basico.task.management.dto.Response;
import basico.task.management.dto.userprofile.ChangePasswordDto;
import basico.task.management.dto.userprofile.ForgotPasswordDto;
import basico.task.management.dto.userprofile.UpdateSupplierInfoDto;
import basico.task.management.dto.userprofile.UserProfileDto;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserProfileController {

    private final UserService userService;
    private final MessageSource messageSource;

    @ApiOperation(value = "Find user by id.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> findById(@PathVariable @NotNull Long id,
                                             @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.user", null, locale), userService.findById(id, locale)));
    }

    @ApiOperation(value = "Find user by user name.")
    @GetMapping(value = "/find/{userName}")
    public ResponseEntity<Response> findByUserName(@PathVariable @NotNull String userName,
                                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.user", null, locale), userService.findByUserName(userName)));
    }

    @ApiOperation(value = "Invite users too register")
    @PostMapping(value = "/sendInvite")
    public ResponseEntity<Response> save(@Valid @RequestBody UserProfileDto user,
                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/users")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("user.create", null, locale), userService.save(user, locale)));
    }

    @ApiOperation(value = "Create admin")
    @PostMapping(value = "/admin")
    public ResponseEntity<Response> saveAdminUser(@RequestParam String userName, @RequestParam String password,
                                                  @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.created(new URI("/users")).body(new Response<>(HttpStatus.CREATED.value(),
                messageSource.getMessage("user.create", null, locale), userService.saveAdminUser(userName, password, locale)));
    }

    @ApiOperation(value = "Update User Profile")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> update(@PathVariable @NotNull Long id, @Valid @RequestBody UserProfileDto user,
                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.update", null, locale), userService.update(id, user, locale)));
    }

    @ApiOperation(value = "Update Supplier Info")
    @PutMapping(value = "/update-supplier-info/{id}")
    public ResponseEntity<Response> updateSupplierInfo(@PathVariable @NotNull Long id, @Valid @RequestBody UpdateSupplierInfoDto supplierInfoDto,
                                                       @RequestHeader("Authorization") String token,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.update", null, locale), userService.updateSupplierInfo(id, supplierInfoDto, token, locale)));
    }

    @ApiOperation(value = "set suppler password")
    @PutMapping(value = "/change-password")
    public ResponseEntity<Response> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.change.password", null, locale), userService.changePassword(changePasswordDto, locale)));
    }

    @ApiOperation(value = "update user status to registered")
    @PutMapping(value = "/{userId}/update-status/{statusName}")
    public ResponseEntity<Response> updateStatus(@PathVariable @NonNull Long userId, @PathVariable @NonNull String statusName,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.update.status", null, locale), userService.updateProfileStatus(userId, statusName)));
    }

    @ApiOperation(value = "update user status to registered")
    @PutMapping(value = "/status-enable/{userId}")
    public ResponseEntity<Response> enableStatus(@PathVariable @NonNull Long userId, @RequestHeader("Authorization") String token,
                                                 @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.update.status", null, locale), userService.enableStatus(userId, token, locale)));
    }

    @ApiOperation(value = "update user status to disable")
    @PutMapping(value = "/status-disable/{userId}")
    public ResponseEntity<Response> disableStatus(@PathVariable @NonNull Long userId, @RequestHeader("Authorization") String token,
                                                  @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("user.update.status", null, locale), userService.disableStatus(userId, token, locale)));
    }

    @GetMapping(value = "/invited")
    public ResponseEntity<Response> findInvitedUsers(@RequestHeader("Authorization") String token,
                                                     @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.fetch", null, locale), userService.findInvitedUsers(token, locale)));

    }

    @GetMapping(value = "/registered")
    public ResponseEntity<Response> findAllRegisteredUsers(@RequestHeader("Authorization") String token,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.fetch", null, locale), userService.findAllRegisteredUsers(token, locale)));

    }

    @GetMapping(value = "/registered/suppliers")
    public ResponseEntity<Response> findAllRegisteredSuppliers(@RequestHeader("Authorization") String token,
                                                           @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.fetch", null, locale), userService.findAllRegisteredSuppliers(token, locale)));

    }

    @GetMapping(value = "/not-active")
    public ResponseEntity<Response> findAllNotActiveUsers(@RequestHeader("Authorization") String token,
                                                          @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.fetch", null, locale), userService.findAllNotActiveUsers(token, locale)));

    }

    @ApiOperation(value = "send forgot password mail")
    @PostMapping(value = "forgotpassword")
    public ResponseEntity<Response> sendforgotpassword(@RequestBody ForgotPasswordDto forgotPassword,
                                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws MessagingException {
        return ResponseEntity.accepted().body(new Response<>(HttpStatus.ACCEPTED.value(),
                messageSource.getMessage("forgot.password", null, locale), userService.forgotPassword(forgotPassword, locale)));
    }

    @ApiOperation(value = "Fetch Supplier Member by id ")
    @GetMapping(value = "/supplier-member/{companyId}")
    public ResponseEntity<Response> getSupplierMember(@PathVariable @NotNull Long companyId,
                                                      @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.member.fetch", null, locale), userService.findSupplierMemberByCompanyId(companyId)));
    }

    @ApiOperation(value = "Fetch All suppler")
    @GetMapping(value = "/supplier")
    public ResponseEntity<Response> getAllSupplier(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("supplier.fetch", null, locale), userService.findAllSupplier()));
    }

    @ApiOperation(value = "Fetch all users by role name")
    @GetMapping(value = "/role/{roleName}")
    public ResponseEntity<Response> findByRoleName(@NotNull @PathVariable String roleName,
                                                   @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("fetch.user", null, locale), userService.findByRoleName(roleName)));
    }

    @ApiOperation(value = "Delete user by id.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable Long id,
                                       @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        userService.delete(id, locale);
        return ResponseEntity.noContent().build();
    }
}
