package com.dmt.bankingapp.service;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.utils.AccountNumberGenerator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public String addNewAccount(Account.AccountType accountType, Client client) {
        String accountNumber = "";
        while (accountNumber.isEmpty()) {
            String generatedNumber = AccountNumberGenerator.generateAccountNumber();
            if (accountRepository.findByAccountNumber(generatedNumber) == null) {
                accountNumber = generatedNumber;
            }
        }

        if (client == null) {
            return "Client not found";
        }

        Account account = new Account(accountNumber, accountType, client);
        accountRepository.save(account);
        return "Account created successfully";
    }

    public Account getLatestAccount() {
        return accountRepository.findTopByOrderByAccountIDDesc();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}
