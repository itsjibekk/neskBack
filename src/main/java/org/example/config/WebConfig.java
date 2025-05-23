package org.example.config;

import jakarta.transaction.Transactional;
import org.example.entity.Role;
import org.example.entity.Tool;
import org.example.repository.RoleRepository;
import org.example.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebMvc
public class WebConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name() // Allow OPTIONS requests
        ));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(-102);
        return bean;
    }
    @Bean
    @Transactional
    CommandLineRunner seedData(RoleRepository roleRepo, ToolRepository toolRepo,@Qualifier("transactionManager") PlatformTransactionManager txManager) {
        return args -> {

            List<String> roleNames = List.of("admin","manager","vl specialist","kl specialist","pro","rtu");
            for (String rn : roleNames) {
                roleRepo.findByName(rn).orElseGet(() -> roleRepo.save(new Role(rn)));
            }

            Map<String,List<String>> roleTools = Map.of(
                    "admin", List.of("user‐mgmt","system‐config"),
                    "manager", List.of("reporting","team‐overview"),
                    "vl specialist", List.of("vl‐tool1","vl‐tool2"),
                    "kl specialist", List.of("kl‐tool1","kl‐tool2"),
                    "pro",           List.of("pro‐tool"),
                    "rtu",           List.of("rtu‐monitor")
            );

            for (var entry : roleTools.entrySet()) {
                Role role = roleRepo.findByName(entry.getKey()).get();
                for (String toolName : entry.getValue()) {
                    // avoid duplicates
                    if (role.getTools().stream().noneMatch(t -> t.getName().equals(toolName))) {
                        Tool t = new Tool();
                        t.setName(toolName);
                        t.setRole(role);
                        toolRepo.save(t);
                    }
                }
            }
        };
    }

}