package com.example.employeebank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * BankAccount Entity - Mapped to the 'bank_accounts' table in MySQL.
 *
 * This entity is linked to Employee via a @OneToOne relationship.
 * Each employee can have exactly one bank account.
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number must not be blank")
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @NotBlank(message = "Bank name must not be blank")
    @Column(nullable = false)
    private String bankName;

    /**
     * Account type examples: SAVINGS, CURRENT, SALARY
     */
    @NotBlank(message = "Account type must not be blank")
    @Column(nullable = false)
    private String accountType;

    @PositiveOrZero(message = "Balance must be zero or a positive number")
    @Column(nullable = false)
    private Double balance;

    // ----- Constructors -----

    public BankAccount() {
    }

    public BankAccount(String accountNumber, String bankName, String accountType, Double balance) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.accountType = accountType;
        this.balance = balance;
    }

    // ----- Getters and Setters -----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}
