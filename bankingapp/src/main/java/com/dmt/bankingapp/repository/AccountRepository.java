package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountNumber(String accountNumber);
}
