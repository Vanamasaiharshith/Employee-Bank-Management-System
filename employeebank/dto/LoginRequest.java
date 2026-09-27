package com.example.employeebank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * LoginRequest - DTO for the POST /auth/login endpoint.
 *
 * The client sends username + password.
 * We validate them and return a JWT token if credentials are correct.
 */
public class LoginRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    private String password;

    // ----- Constructors -----

    public LoginRequest() {
    }

    // ----- Getters and Setters -----

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
