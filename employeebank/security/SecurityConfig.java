package com.example.employeebank.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig - The main Spring Security configuration class.
 *
 * @Configuration - marks this as a configuration class (Spring reads it at startup)
 * @EnableWebSecurity - enables Spring Security for our application
 *
 * This class defines:
 *  1. Which endpoints are PUBLIC (no JWT needed)
 *  2. Which endpoints are PROTECTED (JWT required)
 *  3. The password encoder (BCrypt)
 *  4. The authentication manager
 *  5. Where our JWT filter runs in the security chain
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor Injection
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * SecurityFilterChain - defines the security rules for HTTP requests.
     *
     * Think of this as a bouncer at the door:
     *  - /auth/** → anyone can enter (public)
     *  - /employees/**, /bank-accounts/** → must show JWT token (protected)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (Cross-Site Request Forgery) protection
            // We don't need it because we use JWT (stateless authentication)
            .csrf(csrf -> csrf.disable())

            // Define which URLs are public and which require authentication
            .authorizeHttpRequests(auth -> auth
                // Allow register and login without any token
                .requestMatchers("/auth/**").permitAll()
                // All other requests MUST have a valid JWT token
                .anyRequest().authenticated()
            )

            // Use STATELESS session management
            // This means Spring Security will NOT create HTTP sessions.
            // Each request is authenticated using the JWT token alone.
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Add our JWT filter BEFORE Spring Security's default username/password filter.
            // This ensures the JWT is checked before any other authentication mechanism.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * PasswordEncoder - tells Spring Security to use BCrypt for hashing passwords.
     *
     * BCrypt is a strong hashing algorithm:
     *  - "password123" → "$2a$10$abc123..." (a long, random-looking hash)
     *  - The same password always produces a DIFFERENT hash (due to salting)
     *  - We can never reverse the hash to get the original password
     *  - We only COMPARE: BCrypt.matches("password123", storedHash)
     *
     * @Bean makes this available throughout the application for injection.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager - used to authenticate users during login.
     *
     * Spring Security provides this automatically through AuthenticationConfiguration.
     * We expose it as a @Bean so our AuthService can use it.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
