package com.example.employeebank.service;

import com.example.employeebank.dto.LoginRequest;
import com.example.employeebank.dto.RegisterRequest;
import com.example.employeebank.entity.User;
import com.example.employeebank.exception.GlobalExceptionHandler.InvalidCredentialsException;
import com.example.employeebank.exception.GlobalExceptionHandler.UsernameAlreadyExistsException;
import com.example.employeebank.repository.UserRepository;
import com.example.employeebank.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService - Handles user registration and login logic.
 *
 * @Service tells Spring to create this as a Service bean.
 * Business logic lives here, NOT in the controller.
 *
 * Flow:
 *  Register: Client → AuthController → AuthService → UserRepository → DB
 *  Login:    Client → AuthController → AuthService → UserRepository → JwtService → JWT Token
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // BCryptPasswordEncoder (from SecurityConfig)
    private final JwtService jwtService;

    // Constructor Injection - Spring automatically provides these beans
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Register a new user.
     *
     * Steps:
     *  1. Check if username already exists → throw exception if it does
     *  2. Hash the password using BCrypt (never store plain text!)
     *  3. Create a new User with role "USER"
     *  4. Save to database
     *  5. Return success message
     */
    public String register(RegisterRequest request) {
        // Step 1: Check if username is already taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(
                "Username '" + request.getUsername() + "' is already taken. Please choose another."
            );
        }

        // Step 2: Hash the password using BCrypt
        // "password123" → "$2a$10$abc123..." (secure hash)
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // Step 3 & 4: Create and save the user
        User user = new User(request.getUsername(), hashedPassword, "USER");
        userRepository.save(user);

        return "User registered successfully!";
    }

    /**
     * Login a user and return a JWT token.
     *
     * Steps:
     *  1. Find the user by username → throw exception if not found
     *  2. Verify the password using BCrypt (compare plain text with hash)
     *  3. Generate a JWT token
     *  4. Return the token
     */
    public String login(LoginRequest request) {
        // Step 1: Find user by username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // Step 2: Verify the password
        // passwordEncoder.matches("password123", "$2a$10$...") returns true if they match
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!passwordMatches) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Step 3 & 4: Generate and return JWT token
        return jwtService.generateToken(user.getUsername());
    }
}
