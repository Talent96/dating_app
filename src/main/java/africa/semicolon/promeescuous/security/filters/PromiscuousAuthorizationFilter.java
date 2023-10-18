package africa.semicolon.promeescuous.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static africa.semicolon.promeescuous.utils.AppUtil.APP_NAME;
import static africa.semicolon.promeescuous.utils.AppUtil.getPublicPaths;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
public class PromiscuousAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("I got called because a request to url->{} was made", request.getServletPath());
        if (getPublicPaths().contains(request.getServletPath())) filterChain.doFilter(request, response);
        else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);//"Bearer xxxx_xxxx_xxxx..."


            String token = authorizationHeader.substring("Bearer ".length());

            JWTVerifier verifier = JWT.require(HMAC512("secret"))
                    .withIssuer(APP_NAME)
                    .withClaimPresence("roles")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            List<SimpleGrantedAuthority> authorities = decodedJWT.getClaim("roles")
                    .asList(SimpleGrantedAuthority.class);
            log.info("authorities->{}", authorities);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(null, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }
    }
}
