package com.dmt.bankingapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

@Controller
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/add") // curl.exe -d "giverAccountID=1&receiverAccountID=2&amount=100.0" http://localhost:8080/transaction/add
    public @ResponseBody String addNewTransaction(@RequestParam int giverAccountID, @RequestParam int receiverAccountID, @RequestParam double amount, HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        
        Account giver = accountRepository.findById(giverAccountID).orElse(null);
        if (giver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender's account has not been found");
        }
        if (!giver.getClient().equals(client)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not permitted to transfer money from an account you do not own");
        }

        Account receiver = accountRepository.findById(receiverAccountID).orElse(null);
        if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver's account has not been found");
        }
        
        try {
            Transaction transaction = new Transaction(giver, receiver, amount);
            transactionRepository.save(transaction);
            return "Transaction created successfully! Amount transfered: " + transaction.getAmount();
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(500), e.getMessage());
        }
    }

    @GetMapping("/outgoingTransactions")
    public @ResponseBody String getClientOutgoingTransactions(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        List<Transaction> outgoing = transactionRepository.findByGiver(checking);
        StringBuilder output = new StringBuilder();
        
        for (Transaction transaction : outgoing) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);

            output.append(transaction.getTransactionID())
                        .append(": ")
                        .append(formattedTimestamp)
                        .append("  amount: ")
                        .append(transaction.getAmount())
                        .append("  recipient: ")
                        .append(transaction.getReceiver().getAccountNumber())
                        .append("\n");
        }
        // Remove the last newline in the output
        if (output.length() > 0) {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }

    @GetMapping("/incomingTransactions")
    public @ResponseBody String getClientIncomingTransactions(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        List<Transaction> incoming = transactionRepository.findByReceiver(checking);
        StringBuilder output = new StringBuilder();
        
        for (Transaction transaction : incoming) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);

            output.append(transaction.getTransactionID())
                        .append(": ")
                        .append(formattedTimestamp)
                        .append("  amount: ")
                        .append(transaction.getAmount())
                        .append("  sender: ")
                        .append(transaction.getGiver().getAccountNumber())
                        .append("\n");
        }
        // Remove the last newline in the output
        if (output.length() > 0) {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }

    @GetMapping("/getAll")
    public @ResponseBody String getEveryTransaction(HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        List<Transaction> all = transactionRepository.findAll();
        StringBuilder output = new StringBuilder();

        for (Transaction transaction : all) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);

            output.append(transaction.getTransactionID())
                        .append(": ")
                        .append(formattedTimestamp)
                        .append("  amount: ")
                        .append(transaction.getAmount())
                        .append("  sender: ")
                        .append(transaction.getGiver().getAccountNumber())
                        .append("  recipient: ")
                        .append(transaction.getReceiver().getAccountNumber())
                        .append("\n");
        }

        // Remove the last newline in the output
        if (output.length() > 0) {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }

    @GetMapping("/byAccountNumber")
    public @ResponseBody String getByAccountId(@RequestParam String accountNumber, HttpServletRequest request) {
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
        
        // Format output data to a string
        StringBuilder output = new StringBuilder();

        for (Transaction transaction : transactions) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);

            output.append(transaction.getTransactionID())
                        .append(": ")
                        .append(formattedTimestamp)
                        .append("  amount: ")
                        .append(transaction.getAmount())
                        .append("  sender: ")
                        .append(transaction.getGiver().getAccountNumber())
                        .append("  recipient: ")
                        .append(transaction.getReceiver().getAccountNumber())
                        .append("\n");
        }

        // Remove the last newline in the output
        if (output.length() > 0) {
            output.setLength(output.length() - 1);
        }
        return output.toString();
    }
}