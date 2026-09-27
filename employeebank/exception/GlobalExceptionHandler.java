package com.example.employeebank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler - Catches all exceptions thrown in the application
 * and returns a clean JSON error response instead of a raw stack trace.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *   It intercepts exceptions from ALL controllers and formats the response.
 *
 * Without this class, Spring Boot would return ugly HTML error pages or raw JSON
 * with stack traces. With this class, we return clean, friendly error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================================
    // Custom Exception Classes (defined as inner classes for simplicity)
    // =====================================================================

    /**
     * Thrown when an Employee is not found in the database.
     * Example: GET /employees/999 → "Employee not found with id: 999"
     */
    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a BankAccount is not found in the database.
     */
    public static class BankAccountNotFoundException extends RuntimeException {
        public BankAccountNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when trying to register with a username that already exists.
     */
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when login fails (bad credentials).
     */
    public static class InvalidCredentialsException extends RuntimeException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }

    // =====================================================================
    // Exception Handler Methods
    // =====================================================================

    /**
     * Handles Employee not found → HTTP 404 NOT FOUND
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles BankAccount not found → HTTP 404 NOT FOUND
     */
    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBankAccountNotFound(BankAccountNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles duplicate username during registration → HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles invalid login credentials → HTTP 401 UNAUTHORIZED
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handles @Valid validation failures → HTTP 400 BAD REQUEST
     *
     * When @Valid finds a validation error (e.g., blank name or invalid email),
     * Spring throws MethodArgumentNotValidException.
     *
     * We catch it here and return each field's error message.
     *
     * Example response:
     * {
     *   "email": "Email must be valid",
     *   "name": "Name must not be blank"
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Loop through all field errors and collect them
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles any other unexpected exception → HTTP 500 INTERNAL SERVER ERROR
     *
     * This is a catch-all to prevent raw stack traces from reaching the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
