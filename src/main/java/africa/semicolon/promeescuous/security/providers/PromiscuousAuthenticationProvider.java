package africa.semicolon.promeescuous.security.providers;

import africa.semicolon.promeescuous.exceptions.BadCredentialsException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static africa.semicolon.promeescuous.exceptions.ExceptionMessage.INVALID_CREDENTIALS_EXCEPTION;


@Component
@AllArgsConstructor
public class PromiscuousAuthenticationProvider implements AuthenticationProvider{
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. take the username from the request (contained in authentication) and use
        // the userDetailsService to look for a user from the Db with that username
        String email = authentication.getPrincipal().toString();
        UserDetails user = userDetailsService.loadUserByUsername(email);
        //2. If user from 1 is found, use the PasswordEncoder to compare the password from the
        //request to the users password from the Db
        String password = authentication.getCredentials().toString();
        boolean isValidPasswordMatch = passwordEncoder.matches(password, user.getPassword());
        //3. If the passwords match, request is authenticated
        if (isValidPasswordMatch) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            Authentication authenticationResult = new UsernamePasswordAuthenticationToken(email, password, authorities);
            //return Authentication
            return  authenticationResult;
        }
        //4. else, request isn't authenticated
        throw new BadCredentialsException(INVALID_CREDENTIALS_EXCEPTION.getMessage());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
