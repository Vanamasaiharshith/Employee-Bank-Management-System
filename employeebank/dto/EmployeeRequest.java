package com.example.employeebank.dto;

import jakarta.validation.constraints.*;

/**
 * EmployeeRequest - Data Transfer Object used when CREATING or UPDATING an employee.
 *
 * Why use a DTO instead of the Entity directly?
 *   - The Entity is tied to the database. If we expose it directly in the API,
 *     any change in the DB schema breaks the API.
 *   - DTOs let us control exactly what data the client can send.
 *   - We can add different validation rules for the API without affecting the DB entity.
 *
 * This class holds the data sent by the client in the request body (JSON).
 */
public class EmployeeRequest {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a 10-digit number")
    private String phone;

    @NotBlank(message = "Department must not be blank")
    private String department;

    @Positive(message = "Salary must be a positive number")
    private Double salary;

    // ----- Constructors -----

    public EmployeeRequest() {
    }

    // ----- Getters and Setters -----

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }
}
