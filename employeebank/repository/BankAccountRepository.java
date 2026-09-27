package com.example.employeebank.repository;

import com.example.employeebank.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * BankAccountRepository - Handles all DB operations for BankAccount.
 *
 * JpaRepository<BankAccount, Long> gives us all CRUD methods for free.
 * 'BankAccount' is the entity type, 'Long' is the primary key type.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    /**
     * Spring Data JPA generates SQL for this method automatically:
     *   SELECT * FROM bank_accounts WHERE account_number = ?
     */
    Optional<BankAccount> findByAccountNumber(String accountNumber);
}
