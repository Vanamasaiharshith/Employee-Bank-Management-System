package com.example.employeebank.service;

import com.example.employeebank.dto.BankAccountRequest;
import com.example.employeebank.entity.BankAccount;
import com.example.employeebank.entity.Employee;
import com.example.employeebank.exception.GlobalExceptionHandler.BankAccountNotFoundException;
import com.example.employeebank.exception.GlobalExceptionHandler.EmployeeNotFoundException;
import com.example.employeebank.repository.BankAccountRepository;
import com.example.employeebank.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BankAccountService - Business logic for BankAccount operations.
 *
 * Key Design:
 *   - A BankAccount is always associated with an Employee via @OneToOne.
 *   - When creating a bank account, we link it to the employee.
 *   - The employee table holds the foreign key (bank_account_id).
 */
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final EmployeeRepository employeeRepository;

    // Constructor Injection
    public BankAccountService(BankAccountRepository bankAccountRepository,
                               EmployeeRepository employeeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * CREATE a BankAccount and link it to an Employee.
     *
     * Steps:
     *  1. Find the employee by employeeId
     *  2. Create a new BankAccount entity
     *  3. Save the BankAccount to the database
     *  4. Link the saved BankAccount to the Employee
     *  5. Save the Employee (this updates the bank_account_id foreign key)
     *  6. Return the saved BankAccount
     */
    public BankAccount createBankAccount(BankAccountRequest request) {
        // Step 1: Validate that the employee exists
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(
                    "Employee not found with id: " + request.getEmployeeId()
                ));

        // Step 2 & 3: Create and save the bank account
        BankAccount bankAccount = new BankAccount(
                request.getAccountNumber(),
                request.getBankName(),
                request.getAccountType(),
                request.getBalance()
        );
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        // Step 4 & 5: Link the bank account to the employee and save
        employee.setBankAccount(savedBankAccount);
        employeeRepository.save(employee);

        return savedBankAccount;
    }

    /**
     * GET all bank accounts.
     */
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    /**
     * GET a single BankAccount by ID.
     */
    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(
                    "Bank account not found with id: " + id
                ));
    }

    /**
     * GET the BankAccount of a specific Employee.
     *
     * This is used by GET /employees/{employeeId}/bank-account
     */
    public BankAccount getBankAccountByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(
                    "Employee not found with id: " + employeeId
                ));

        BankAccount bankAccount = employee.getBankAccount();
        if (bankAccount == null) {
            throw new BankAccountNotFoundException(
                "No bank account found for employee with id: " + employeeId
            );
        }
        return bankAccount;
    }

    /**
     * UPDATE a BankAccount's details.
     *
     * Note: We only update bank account fields here.
     * The employee link is not changed during an update.
     */
    public BankAccount updateBankAccount(Long id, BankAccountRequest request) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(
                    "Bank account not found with id: " + id
                ));

        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setBankName(request.getBankName());
        bankAccount.setAccountType(request.getAccountType());
        bankAccount.setBalance(request.getBalance());

        return bankAccountRepository.save(bankAccount);
    }

    /**
     * DELETE a BankAccount by ID.
     *
     * We also unlink the bank account from the employee before deleting,
     * to avoid foreign key constraint violations.
     */
    public void deleteBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(
                    "Bank account not found with id: " + id
                ));

        // Find the employee linked to this bank account and unlink it
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            if (employee.getBankAccount() != null &&
                employee.getBankAccount().getId().equals(id)) {
                employee.setBankAccount(null);
                employeeRepository.save(employee);
                break;
            }
        }

        bankAccountRepository.deleteById(id);
    }
}
