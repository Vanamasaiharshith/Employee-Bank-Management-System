package com.example.employeebank.controller;

import com.example.employeebank.dto.LoginRequest;
import com.example.employeebank.dto.RegisterRequest;
import com.example.employeebank.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController - Handles user registration and login endpoints.
 *
 * @RestController = @Controller + @ResponseBody
 *   - @Controller: marks this as a Spring MVC controller
 *   - @ResponseBody: the return value is written directly to the HTTP response body as JSON
 *
 * @RequestMapping("/auth") - all endpoints in this class start with /auth
 *
 * These endpoints are PUBLIC (no JWT required), as configured in SecurityConfig.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // Constructor Injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /auth/register
     *
     * Register a new user.
     *
     * @Valid triggers bean validation on the RegisterRequest object.
     * If validation fails, GlobalExceptionHandler catches the exception
     * and returns a 400 error with field-level messages.
     *
     * @RequestBody reads the JSON from the request body and maps it to RegisterRequest.
     *
     * Example request:
     * POST /auth/register
     * {
     *   "username": "vijay",
     *   "password": "password123"
     * }
     *
     * Example response (201 CREATED):
     * {
     *   "message": "User registered successfully!"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        String message = authService.register(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /auth/login
     *
     * Login with username and password. Returns a JWT token on success.
     *
     * Example request:
     * POST /auth/login
     * {
     *   "username": "vijay",
     *   "password": "password123"
     * }
     *
     * Example response (200 OK):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9..."
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response); // ResponseEntity.ok() returns HTTP 200
    }
}
