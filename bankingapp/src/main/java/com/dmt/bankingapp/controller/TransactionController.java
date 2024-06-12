package com.dmt.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.repository.AccountRepository;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/add") // curl.exe -d "giverAccountID=1&receiverAccountID=2&amount=100.0" http://localhost:8080/transaction/add
    public @ResponseBody String addNewTransaction(@RequestParam int giverAccountID, @RequestParam int receiverAccountID, @RequestParam double amount) {
        Account giver = accountRepository.findById(giverAccountID).orElse(null);
        Account receiver = accountRepository.findById(receiverAccountID).orElse(null);
        if (giver == null || receiver == null) {
            return "Giver or Receiver account not found";
        }
        
        try {
            Transaction transaction = new Transaction(giver, receiver, amount);
            transactionRepository.save(transaction);
            return "Transaction created successfully";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/all")
    public @ResponseBody List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/byTransactionID")
    public @ResponseBody Transaction getByTransactionID(@RequestParam int transactionID) {
        return transactionRepository.findById(transactionID).orElse(null);
    }
}
