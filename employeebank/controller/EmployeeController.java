package com.example.employeebank.controller;

import com.example.employeebank.dto.EmployeeRequest;
import com.example.employeebank.dto.EmployeeResponse;
import com.example.employeebank.entity.BankAccount;
import com.example.employeebank.entity.Employee;
import com.example.employeebank.service.BankAccountService;
import com.example.employeebank.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmployeeController - Handles all REST API endpoints for Employee.
 *
 * ALL endpoints here require JWT authentication (configured in SecurityConfig).
 *
 * This controller delegates ALL business logic to EmployeeService.
 * The controller's job is only:
 *   1. Receive the HTTP request
 *   2. Call the appropriate service method
 *   3. Return the HTTP response
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final BankAccountService bankAccountService;

    // Constructor Injection
    public EmployeeController(EmployeeService employeeService, BankAccountService bankAccountService) {
        this.employeeService = employeeService;
        this.bankAccountService = bankAccountService;
    }

    /**
     * POST /employees
     * Create a new employee.
     *
     * @Valid - validates the request body (checks @NotBlank, @Email, etc.)
     * @RequestBody - reads JSON from request body and maps to EmployeeRequest
     *
     * Example request body:
     * {
     *   "name": "Vijay",
     *   "email": "vijay@gmail.com",
     *   "phone": "9876543210",
     *   "department": "IT",
     *   "salary": 40000
     * }
     *
     * Returns HTTP 201 CREATED with a success message and the new employee's ID.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse savedEmployee = employeeService.createEmployee(request);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Employee created successfully");
        response.put("employeeId", savedEmployee.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /employees
     * Get all employees from the database.
     *
     * Returns HTTP 200 OK with a list of all employees.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    /**
     * GET /employees/{id}
     * Get a single employee by their ID.
     *
     * @PathVariable - extracts the {id} from the URL path
     * Example: GET /employees/1 → id = 1
     *
     * Returns HTTP 200 OK if found, HTTP 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * PUT /employees/{id}
     * Update an existing employee's details.
     *
     * @PathVariable - gets the employee ID from the URL
     * @RequestBody - gets the new data from the request body
     *
     * Returns HTTP 200 OK with the updated employee data.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    /**
     * DELETE /employees/{id}
     * Delete an employee by their ID.
     * Also deletes the linked BankAccount (due to CascadeType.ALL on @OneToOne).
     *
     * Returns HTTP 200 OK with a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * GET /employees/{employeeId}/bank-account
     * Get the bank account of a specific employee.
     *
     * Example: GET /employees/1/bank-account
     *   → Returns the bank account linked to employee with ID 1
     *
     * Returns HTTP 200 OK with the bank account data.
     * Returns HTTP 404 if employee or bank account not found.
     */
    @GetMapping("/{employeeId}/bank-account")
    public ResponseEntity<BankAccount> getBankAccountByEmployee(@PathVariable Long employeeId) {
        BankAccount bankAccount = bankAccountService.getBankAccountByEmployeeId(employeeId);
        return ResponseEntity.ok(bankAccount);
    }
}
