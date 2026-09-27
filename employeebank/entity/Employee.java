package com.example.employeebank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Employee Entity - Mapped to the 'employees' table in MySQL.
 *
 * @Entity  tells JPA/Hibernate that this class represents a database table.
 * @Table   specifies the exact table name in the database.
 */
@Entity
@Table(name = "employees")
public class Employee {

    /**
     * @Id marks this field as the Primary Key.
     * @GeneratedValue(strategy = GenerationType.IDENTITY)
     *   tells the DB to auto-increment this value (1, 2, 3, ...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be a 10-digit number")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Department must not be blank")
    @Column(nullable = false)
    private String department;

    @Positive(message = "Salary must be a positive number")
    @Column(nullable = false)
    private Double salary;

    /**
     * One Employee has One BankAccount.
     *
     * @OneToOne - JPA relationship annotation
     * @JoinColumn - this table (employees) will have a foreign key column
     *               called 'bank_account_id' pointing to the bank_accounts table.
     * cascade = CascadeType.ALL - if we delete an employee, their bank account is also deleted.
     * orphanRemoval = true - removes the bank account record if it's no longer linked to an employee.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    // ----- Constructors -----

    // Default constructor is required by JPA
    public Employee() {
    }

    public Employee(String name, String email, String phone, String department, Double salary) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.salary = salary;
    }

    // ----- Getters and Setters -----
    // (These allow Spring and JPA to read and write the fields)

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

    public BankAccount getBankAccount() { return bankAccount; }
    public void setBankAccount(BankAccount bankAccount) { this.bankAccount = bankAccount; }
}
