package com.dmt.bankingapp.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

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
            return "Transaction created successfully! Amount transfered: " + transaction.getAmount();
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/outgoingTransactions")
    public @ResponseBody List<Transaction> getClientOutgoingTransactions(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        return transactionRepository.findByGiver(checking);
    }

    @GetMapping("/incomingTransactions")
    public @ResponseBody List<Transaction> getClientIncomingTransactions(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        return transactionRepository.findByReceiver(checking);
    }

    @GetMapping("/everyTransaction")
    public @ResponseBody List<Transaction> getEveryTransaction(HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        return transactionRepository.findAll();
    }

    @GetMapping("/byAccountNumber")
    public @ResponseBody List<Transaction> getByAccountId(@RequestParam String accountNumber, HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NoSuchElementException("Account not found with account number: " + accountNumber);
        }
        
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findByGiver(account));
        transactions.addAll(transactionRepository.findByReceiver(account));
        
        // Sort transactions by timestamp in ascending order
        transactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
        
        return transactions;
    }
}
