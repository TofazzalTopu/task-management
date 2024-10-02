package basico.task.management.controller;

import basico.task.management.constant.AppConstants;
import basico.task.management.dto.LoginDto;
import basico.task.management.dto.LoginResponse;
import basico.task.management.dto.Response;
import basico.task.management.exception.Messages;
import basico.task.management.service.LDAPUserService;
import basico.task.management.service.LoginService;
import basico.task.management.service.UserService;
import basico.task.management.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "user/login")
public class LoginController {

    private final LoginService loginService;
    private final UserService userService;
    private final LDAPUserService ldapUserService;
    private final MessageSource messageSource;

    @Value("${password.decode.key}")
    String PASSWORD_DECODE_KEY;

    @Value("${password.decode.iv}")
    String PASSWORD_DECODE_IV;


    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginDto loginDto,
                                                         @RequestHeader(name = "Accept-Language", required = false) Locale locale) throws Exception {
        String originalPassword = PasswordUtil.decode(loginDto.getPassword(), PASSWORD_DECODE_KEY, PASSWORD_DECODE_IV);
        String group = ldapUserService.checkLdapGroup(loginDto.getUsername(), originalPassword);
        if (group != null && !group.equalsIgnoreCase(AppConstants.LDAP_GROUP_NOT_FOUND)) {
            userService.saveLdapUser(loginDto.getUsername(), originalPassword, group, locale);
        }
        LoginResponse loginResponse = loginService.login(loginDto);
        userService.updateLoginTime(loginDto.getUsername());   // we have in memory authentication for two user that is not from database
        return ResponseEntity.ok().body(new Response<>(HttpStatus.OK.value(),
                messageSource.getMessage("login.success", null, locale), loginResponse));
    }

}
