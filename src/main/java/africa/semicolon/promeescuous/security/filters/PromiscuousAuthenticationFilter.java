package africa.semicolon.promeescuous.security.filters;

import africa.semicolon.promeescuous.dtos.requests.LoginRequest;
import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import africa.semicolon.promeescuous.exceptions.PromiscuousBaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static africa.semicolon.promeescuous.utils.JwtUtil.generateAccessToken;
import static africa.semicolon.promeescuous.utils.JwtUtil.generateVerificationToken;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class PromiscuousAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //1. extract the authentication credentials from the request
            InputStream inputStream = request.getInputStream();
            LoginRequest loginRequest = objectMapper.readValue(inputStream, LoginRequest.class);
            log.info("credentials-->{}", loginRequest);
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();
            //2. create an authentication object that is not yet authenticated
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            //3. delegate authentication responsibility of the authentication object to the authentication manager
            Authentication authenticationResult = authenticationManager.authenticate(authentication);
            //4. put the now authenticated object in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            return authenticationResult;
        } catch (IOException e) {
            throw new PromiscuousBaseException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        Collection<?extends GrantedAuthority> userAuthorities = authResult.getAuthorities();
        List<? extends GrantedAuthority> authorities = new ArrayList<>(userAuthorities);
        var roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String token = generateAccessToken(roles);
        var apiResponse = ApiResponse.builder().data(token).build();
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
