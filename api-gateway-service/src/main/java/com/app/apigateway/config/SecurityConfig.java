package com.app.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for API Gateway.
 * Configures CORS and permits all requests (gateway handles auth via JWT validation).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for API gateway)
            .csrf(csrf -> csrf.disable())

            // Enable CORS (uses CorsConfigurationSource bean from WebConfig)
            .cors(cors -> {})

            // Stateless session (no server-side sessions)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow OPTIONS preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Allow all other requests (JWT validation happens in downstream services)
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
