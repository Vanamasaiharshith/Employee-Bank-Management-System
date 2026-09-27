package com.example.employeebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of the Spring Boot application.
 *
 * @SpringBootApplication combines three annotations:
 *   - @Configuration: marks this as a source of bean definitions
 *   - @EnableAutoConfiguration: tells Spring Boot to auto-configure the app
 *   - @ComponentScan: scans all classes in this package and sub-packages
 */
@SpringBootApplication
public class EmployeeBankManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeBankManagementApplication.class, args);
    }
}
