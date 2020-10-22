package com.project.DigitalBank.repositories;

import com.project.DigitalBank.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findOneByAccountNumber(String accountNumber);
}
