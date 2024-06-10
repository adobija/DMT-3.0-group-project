package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findByAccountOfSender(Integer accountOfSender);
    List<Transaction> findByAccountOfReceiver(Integer accountOfReceiver);

}
