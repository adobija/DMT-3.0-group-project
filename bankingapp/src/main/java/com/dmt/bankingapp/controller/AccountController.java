package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.utils.AccountNumberGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientController clientController;

    @PostMapping("/add")
    public @ResponseBody String addNewAccount(@RequestParam Account.AccountType accountType, @RequestParam int clientId) {
        String accountNumber = ""; 
        String generatedNumber = "";
        while (accountNumber.isEmpty()){
            generatedNumber = AccountNumberGenerator.generateAccountNumber();
            if (accountRepository.findByAccountNumber(generatedNumber) == null) {
                accountNumber = generatedNumber;
            }
        }

        Client client = clientController.getByClientID(clientId);
        
        Account account = new Account(accountNumber, accountType, client);
        accountRepository.save(account);
        return "Account created successfully";
    }

    @PostMapping("/updateBalance")
    public @ResponseBody String updateBalance(@RequestParam int accountId, @RequestParam double amount, @RequestParam boolean isExpense) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            account.setAccountBalance(amount, isExpense);
            accountRepository.save(account);
            return "Account balance updated successfully";
        } else {
            return "Account not found";
        }
    }

    @DeleteMapping("/remove")
    public @ResponseBody String removeAccount(@RequestParam int accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            accountRepository.delete(account);
            return "Account deleted successfully";
        } else {
            return "Account not found";
        }
    }

    @GetMapping("/all")
    public @ResponseBody List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/byAccountNumber")
    public @ResponseBody Account getAccountByNumber(@RequestParam String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}
