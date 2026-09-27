package com.example.employeebank.service;

import com.example.employeebank.dto.EmployeeRequest;
import com.example.employeebank.dto.EmployeeResponse;
import com.example.employeebank.entity.Employee;
import com.example.employeebank.exception.GlobalExceptionHandler.EmployeeNotFoundException;
import com.example.employeebank.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EmployeeService - Contains all the business logic for Employee operations.
 *
 * The Controller calls methods in this class.
 * This class calls the Repository to talk to the database.
 *
 * Flow: Controller → Service → Repository → MySQL
 *
 * Why separate Controller and Service?
 *   - Controller should only handle HTTP requests/responses.
 *   - Business logic should live in the Service.
 *   - This separation makes the code easier to test and maintain.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor Injection
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * CREATE a new Employee.
     *
     * Steps:
     *  1. Convert the EmployeeRequest DTO to an Employee entity
     *  2. Save to database using repository
     *  3. Convert the saved entity to EmployeeResponse DTO
     *  4. Return the response
     */
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        // Convert DTO → Entity
        Employee employee = new Employee(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getDepartment(),
                request.getSalary()
        );

        // Save to DB (JPA generates: INSERT INTO employees ...)
        Employee savedEmployee = employeeRepository.save(employee);

        // Convert Entity → DTO and return
        return convertToResponse(savedEmployee);
    }

    /**
     * GET all employees from the database.
     *
     * JPA generates: SELECT * FROM employees
     *
     * We convert each Employee entity to an EmployeeResponse DTO
     * using Java Streams for clean, readable code.
     */
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToResponse)   // for each employee, call convertToResponse
                .collect(Collectors.toList());   // collect results into a List
    }

    /**
     * GET a single Employee by their ID.
     *
     * JPA generates: SELECT * FROM employees WHERE id = ?
     *
     * If not found, we throw EmployeeNotFoundException.
     * The GlobalExceptionHandler catches this and returns HTTP 404.
     */
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        return convertToResponse(employee);
    }

    /**
     * UPDATE an existing Employee.
     *
     * Steps:
     *  1. Find the employee by ID (throw 404 if not found)
     *  2. Update the fields with new values from the request
     *  3. Save the updated employee (JPA generates: UPDATE employees SET ... WHERE id = ?)
     *  4. Return the updated employee as a response
     */
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        // Update fields
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToResponse(updatedEmployee);
    }

    /**
     * DELETE an Employee by ID.
     *
     * JPA generates: DELETE FROM employees WHERE id = ?
     */
    public void deleteEmployee(Long id) {
        // First check if the employee exists, throw 404 if not
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    /**
     * GET the BankAccount linked to a specific Employee.
     *
     * Returns the Employee entity so the controller/caller can access bankAccount.
     */
    public Employee getEmployeeEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Helper method: Convert Employee Entity → EmployeeResponse DTO
     *
     * We don't expose the Employee entity directly to the client.
     * Instead, we copy only the fields we want to return.
     */
    private EmployeeResponse convertToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }
}
