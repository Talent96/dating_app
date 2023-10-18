package africa.semicolon.promeescuous.security;


import africa.semicolon.promeescuous.security.filters.PromiscuousAuthenticationFilter;
import africa.semicolon.promeescuous.security.filters.PromiscuousAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static africa.semicolon.promeescuous.models.Role.CUSTOMER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        final String[] publicEndPoints = {"/api/v1/user", "/login"};
        return httpSecurity
                .addFilterAt(new PromiscuousAuthenticationFilter(authenticationManager),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new PromiscuousAuthorizationFilter(),
                        PromiscuousAuthenticationFilter.class)
                .sessionManagement(customizer->customizer.sessionCreationPolicy(STATELESS))
                .csrf(c->c.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(c->c.requestMatchers(POST, publicEndPoints).permitAll())
                .authorizeHttpRequests(c->c.anyRequest().hasAuthority(CUSTOMER.name()))
                .build();
    }


}
