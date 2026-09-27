package com.example.employeebank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * RegisterRequest - DTO for the POST /auth/register endpoint.
 *
 * The client sends a username and password to create a new account.
 * The password must be at least 6 characters long.
 */
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    // ----- Constructors -----

    public RegisterRequest() {
    }

    // ----- Getters and Setters -----

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
