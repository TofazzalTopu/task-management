package basico.task.management.service.impl;

import basico.task.management.config.security.TokenProvider;
import basico.task.management.dto.LoginDto;
import basico.task.management.dto.LoginResponse;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.service.LoginService;
import basico.task.management.service.UserService;
import basico.task.management.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Value("${password.decode.key}")
    String PASSWORD_DECODE_KEY;

    @Value("${password.decode.iv}")
    String PASSWORD_DECODE_IV;

    @Override
    public LoginResponse login(LoginDto loginDto) throws Exception {
        LoginResponse response = new LoginResponse();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        PasswordUtil.decode(loginDto.getPassword(), PASSWORD_DECODE_KEY, PASSWORD_DECODE_IV)
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<UserProfile> userProfile = userService.findByUserName(authentication.getName());
        if (userProfile.isPresent()) {
            final String token = jwtTokenUtil.generateToken(authentication, userProfile.get().getId(), userProfile.get().getRole().getId());
            response.setUserId(userProfile.get().getId());
            if (userProfile.get().isSupplier()) {
                response.setCompanyName(userProfile.get().getCompany());
                response.setSupplierInfoIsUpdated(userProfile.get().isSupplierInfoIsUpdated());
                if (Objects.nonNull(userProfile.get().getCompanyId()) && userProfile.get().getCompanyId() > 1)
                    response.setMember(true);
            }
            response.setRole(userProfile.get().getRole());
            response.setToken(token);
        }
        response.setUserName(authentication.getName());
        response.setSupplier(false);
        return response;
    }

}
