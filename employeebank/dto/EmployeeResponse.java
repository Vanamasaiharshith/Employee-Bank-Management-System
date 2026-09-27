package com.example.employeebank.dto;

/**
 * EmployeeResponse - DTO used when RETURNING employee data to the client.
 *
 * We return this instead of the raw Employee entity so we control
 * exactly what data is exposed in the API response.
 */
public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Double salary;

    // ----- Constructors -----

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String name, String email,
                             String phone, String department, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.salary = salary;
    }

    // ----- Getters and Setters -----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
