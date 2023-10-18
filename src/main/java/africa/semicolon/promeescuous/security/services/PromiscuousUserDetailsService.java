package africa.semicolon.promeescuous.security.services;

import africa.semicolon.promeescuous.models.User;
import africa.semicolon.promeescuous.security.models.SecureUser;
import africa.semicolon.promeescuous.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PromiscuousUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        UserDetails userDetails = new SecureUser(user);
        return userDetails;
    }
}
