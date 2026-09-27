package com.example.employeebank.repository;

import com.example.employeebank.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * EmployeeRepository - Handles all database operations for Employee.
 *
 * By extending JpaRepository<Employee, Long>, we get these methods for FREE:
 *   - save(employee)         → INSERT or UPDATE
 *   - findById(id)           → SELECT by ID
 *   - findAll()              → SELECT all rows
 *   - deleteById(id)         → DELETE by ID
 *   - existsById(id)         → Check if record exists
 *   - count()                → Count all records
 *
 * Spring Data JPA automatically implements all these methods at runtime.
 * We don't write any SQL!
 *
 * @Repository tells Spring to treat this as a repository component
 *             and also translates database exceptions into Spring exceptions.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Custom query method - Spring Data JPA reads the method name and
     * automatically generates the SQL:
     *   SELECT * FROM employees WHERE email = ?
     *
     * No SQL needed - the method name IS the query!
     */
    Optional<Employee> findByEmail(String email);
}
