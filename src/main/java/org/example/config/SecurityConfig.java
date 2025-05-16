package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthProvider userAuthProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JWTAuthFilter(userAuthProvider), BasicAuthenticationFilter.class)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/transformers/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/roles").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/users").hasRole("admin")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("admin")
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
