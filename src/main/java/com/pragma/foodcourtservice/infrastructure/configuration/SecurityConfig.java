package com.pragma.foodcourtservice.infrastructure.configuration;

import com.pragma.foodcourtservice.domain.model.Role;
import com.pragma.foodcourtservice.infrastructure.output.security.helper.CustomAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity()
public class SecurityConfig {

    private final CustomAuthenticationFilter customAuthenticationFilter;

    public SecurityConfig(CustomAuthenticationFilter customAuthenticationFilter) {
        this.customAuthenticationFilter = customAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/restaurants").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/restaurants/employee-assignment").hasRole(Role.OWNER.name())
                        .requestMatchers(HttpMethod.POST, "/dishes").hasRole(Role.OWNER.name())
                        .requestMatchers(HttpMethod.PUT, "/dishes/**").hasRole(Role.OWNER.name())
                        .requestMatchers(HttpMethod.GET, "/restaurants").authenticated()
                        .requestMatchers(HttpMethod.POST, "/orders").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/orders/*/cancel").hasRole(Role.CLIENT.name())
                        .requestMatchers(HttpMethod.PUT, "/orders/**").hasRole(Role.EMPLOYEE.name())
                        .anyRequest().permitAll()
                );

        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
