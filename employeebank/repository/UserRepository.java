package com.example.employeebank.repository;

import com.example.employeebank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * UserRepository - Handles database operations for User (login/register).
 *
 * Spring Security calls findByUsername() when a user tries to log in,
 * to load their details from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Used during login to find the user by their username.
     * SQL generated: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Used during registration to check if a username already exists.
     * SQL generated: SELECT COUNT(*) > 0 FROM users WHERE username = ?
     */
    boolean existsByUsername(String username);
}
