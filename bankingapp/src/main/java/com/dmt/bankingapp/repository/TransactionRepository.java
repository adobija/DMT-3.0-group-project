package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
//    List<Transaction> findByAccountOfSender(Integer accountOfSender);
//    List<Transaction> findByAccountOfReceiver(Integer accountOfReceiver);

}
