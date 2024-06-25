package com.dmt.bankingapp.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.dmt.bankingapp.record.transactions.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PostMapping("/add")
    public String addNewTransaction(@RequestParam String giverAccountNumber, @RequestParam String receiverAccountNumber, @RequestParam double amount, HttpServletRequest request, Model model) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        
        Account giver = accountRepository.findByAccountNumber(giverAccountNumber);
        if (giver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender's account has not been found");
        }
        if (!giver.getClient().equals(client)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not permitted to transfer money from an account you do not own");
        }

        Account receiver = accountRepository.findByAccountNumber(receiverAccountNumber);
        if (receiver == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver's account has not been found");
        }
        model.addAttribute("clientAccount", client.getCheckingAccount().getAccountNumber());
        try {
            Transaction transaction = new Transaction(giver, receiver, amount);
            transactionRepository.save(transaction);
            String output = "Transaction created successfully! Amount transfered: " + transaction.getAmount();
            model.addAttribute("add", output);
            return "transactionTemplates/add";
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(500), e.getMessage());
        }
    }

    @GetMapping("/outgoingTransactions")
    public String getClientOutgoingTransactions(HttpServletRequest request, Model model) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        List<Transaction> outgoing = transactionRepository.findByGiver(checking);

        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();
        for (Transaction transaction : outgoing) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);
            transactionRecords.add(new TransactionRecord(transaction.getTransactionID(), formattedTimestamp, transaction.getAmount(), checking.getAccountNumber(), transaction.getReceiver().getAccountNumber()));
        }

        model.addAttribute("outgoing", transactionRecords);
        return "transactionTemplates/outgoing";
    }

    @GetMapping("/incomingTransactions")
    public String getClientIncomingTransactions(HttpServletRequest request, Model model) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Account checking = client.getCheckingAccount();
        List<Transaction> incoming = transactionRepository.findByReceiver(checking);


        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();
        for (Transaction transaction : incoming) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);
            transactionRecords.add(new TransactionRecord(transaction.getTransactionID(), formattedTimestamp, transaction.getAmount(), transaction.getGiver().getAccountNumber(), checking.getAccountNumber()));

        }
        model.addAttribute("incoming",transactionRecords);
        return "transactionTemplates/incoming";
    }

    @GetMapping("/getAll")
    public String getEveryTransaction(HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        List<Transaction> all = transactionRepository.findAll();

        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();
        for (Transaction transaction : all) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);
            transactionRecords.add(new TransactionRecord(transaction.getTransactionID(), formattedTimestamp, transaction.getAmount(), transaction.getGiver().getAccountNumber(), transaction.getReceiver().getAccountNumber()));
        }

        // Remove the last newline in the output
        model.addAttribute("getAll", transactionRecords);
        return "transactionTemplates/getAll";
    }

    @GetMapping("/byAccountNumber")
    public String getByAccountId(@RequestParam String accountNumber, HttpServletRequest request, Model model) {
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
        ArrayList<TransactionRecord> transactionRecords = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDateTime timestamp = transaction.getTimestamp();
            String formattedTimestamp = timestamp.format(formatter);
            transactionRecords.add(new TransactionRecord(transaction.getTransactionID(), formattedTimestamp, transaction.getAmount(), transaction.getGiver().getAccountNumber(), transaction.getReceiver().getAccountNumber()));
        }

        model.addAttribute("accNumber", transactionRecords);
        return "transactionTemplates/accNumber";
    }
}