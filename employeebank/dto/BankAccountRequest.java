package com.example.employeebank.dto;

import jakarta.validation.constraints.*;

/**
 * BankAccountRequest - DTO used when CREATING or UPDATING a bank account.
 *
 * The client sends this JSON body when calling POST /bank-accounts or PUT /bank-accounts/{id}.
 * We also include an employeeId so we know which employee to link this account to.
 */
public class BankAccountRequest {

    @NotBlank(message = "Account number must not be blank")
    private String accountNumber;

    @NotBlank(message = "Bank name must not be blank")
    private String bankName;

    @NotBlank(message = "Account type must not be blank")
    private String accountType;

    @PositiveOrZero(message = "Balance must be zero or a positive number")
    private Double balance;

    /**
     * The ID of the employee this bank account belongs to.
     * When creating a bank account, we link it to this employee.
     */
    @NotNull(message = "Employee ID must not be null")
    private Long employeeId;

    // ----- Constructors -----

    public BankAccountRequest() {
    }

    // ----- Getters and Setters -----

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
}
