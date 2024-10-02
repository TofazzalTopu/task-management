package basico.task.management.config.security;

import basico.task.management.exception.Messages;
import basico.task.management.model.primary.UserProfile;
import basico.task.management.repository.primary.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;
import java.util.List;

import static basico.task.management.constant.AppConstants.*;


@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final Messages messages;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TO DO add db config for database authentication
        Optional<UserProfile> userProfile = userProfileRepository.findByUserNameAndSupplier(username, true);
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        String password = null;
        if (userProfile.isEmpty()) {
            if (username.equals(ADMIN_USERNAME)) {
                password = ADMIN_PASSWORD;
                grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_NAME_ADMIN));
            } else {
                throw new UsernameNotFoundException(messages.get("user.not.found"));
            }
        } else {
            UserProfile user = userProfile.get();
            password = user.getPassword();
            grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_NAME_SUPPLIER));
        }
        return new org.springframework.security.core.userdetails.User(username.toLowerCase(),
                password,
                true,
                true,
                true,
                true,
                grantedAuthorities);

    }

}
