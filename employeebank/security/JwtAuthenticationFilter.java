package com.example.employeebank.security;

import com.example.employeebank.entity.User;
import com.example.employeebank.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtAuthenticationFilter - Intercepts every HTTP request and checks for a valid JWT token.
 *
 * This filter runs BEFORE the request reaches any controller.
 * It extends OncePerRequestFilter, meaning it runs exactly once per request.
 *
 * How it works:
 *  1. Check if the request has an "Authorization: Bearer <token>" header
 *  2. Extract the token from the header
 *  3. Extract the username from the token using JwtService
 *  4. Validate the token
 *  5. If valid, set the user as authenticated in Spring Security's context
 *  6. Let the request continue to the controller
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    // Constructor Injection - Spring automatically provides these dependencies
    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Read the Authorization header from the request
        // Example: "Authorization: Bearer eyJhbGci..."
        String authorizationHeader = request.getHeader("Authorization");

        // Step 2: If there's no Authorization header or it doesn't start with "Bearer ", skip this filter
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // pass the request to the next filter/controller
            return;
        }

        // Step 3: Extract the actual token by removing "Bearer " (7 characters) from the beginning
        String token = authorizationHeader.substring(7);

        // Step 4: Extract the username from the token
        String username;
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            // Token is malformed or invalid - reject the request
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5: If we got a username AND the user is NOT already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from the database
            User user = userRepository.findByUsername(username).orElse(null);

            // Step 6: Validate the token
            if (user != null && jwtService.isTokenValid(token, username)) {

                // Step 7: Create an authentication object for Spring Security
                // SimpleGrantedAuthority represents the user's role (e.g., ROLE_USER)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null, // credentials (not needed after login)
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                        );

                // Add request details (IP address, session info) to the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 8: Store the authentication in Spring Security's context
                // This tells Spring Security: "This user is authenticated for this request"
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Step 9: Pass the request to the next filter or the controller
        filterChain.doFilter(request, response);
    }
}
