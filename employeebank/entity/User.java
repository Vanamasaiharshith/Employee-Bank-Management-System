package com.example.employeebank.entity;

import jakarta.persistence.*;

/**
 * User Entity - Stores login credentials in the 'users' table.
 *
 * This entity is used by Spring Security for authentication.
 * When a user registers, their username and BCrypt-hashed password are saved here.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username must be unique because we use it to identify users during login.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Password is stored as a BCrypt hash, never as plain text.
     * Example: "password123" becomes "$2a$10$..."
     */
    @Column(nullable = false)
    private String password;

    /**
     * Every user gets the role "USER".
     * This is used by Spring Security to authorize access.
     */
    @Column(nullable = false)
    private String role;

    // ----- Constructors -----

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ----- Getters and Setters -----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
