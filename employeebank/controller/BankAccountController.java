package com.example.employeebank.controller;

import com.example.employeebank.dto.BankAccountRequest;
import com.example.employeebank.entity.BankAccount;
import com.example.employeebank.service.BankAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BankAccountController - Handles all REST API endpoints for BankAccount.
 *
 * ALL endpoints here require JWT authentication (configured in SecurityConfig).
 *
 * Base URL: /bank-accounts
 */
@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    // Constructor Injection
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * POST /bank-accounts
     * Create a new bank account and link it to an employee.
     *
     * Example request body:
     * {
     *   "accountNumber": "1234567890",
     *   "bankName": "HDFC",
     *   "accountType": "SAVINGS",
     *   "balance": 50000,
     *   "employeeId": 1
     * }
     *
     * Returns HTTP 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBankAccount(
            @Valid @RequestBody BankAccountRequest request) {

        BankAccount savedAccount = bankAccountService.createBankAccount(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bank account created successfully");
        response.put("accountId", savedAccount.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /bank-accounts
     * Get all bank accounts.
     *
     * Returns HTTP 200 OK with a list of all bank accounts.
     */
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * GET /bank-accounts/{id}
     * Get a single bank account by its ID.
     *
     * @PathVariable extracts {id} from the URL.
     * Example: GET /bank-accounts/1 → id = 1
     *
     * Returns HTTP 200 OK if found, HTTP 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.getBankAccountById(id);
        return ResponseEntity.ok(bankAccount);
    }

    /**
     * PUT /bank-accounts/{id}
     * Update an existing bank account.
     *
     * Returns HTTP 200 OK with the updated bank account data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateBankAccount(
            @PathVariable Long id,
            @Valid @RequestBody BankAccountRequest request) {

        BankAccount updatedAccount = bankAccountService.updateBankAccount(id, request);
        return ResponseEntity.ok(updatedAccount);
    }

    /**
     * DELETE /bank-accounts/{id}
     * Delete a bank account by its ID.
     *
     * Returns HTTP 200 OK with a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Bank account deleted successfully");

        return ResponseEntity.ok(response);
    }
}
