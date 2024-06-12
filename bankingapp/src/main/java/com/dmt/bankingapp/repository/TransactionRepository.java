package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByGiver(Account giver);
    List<Transaction> findByReceiver(Account receiver);
}
