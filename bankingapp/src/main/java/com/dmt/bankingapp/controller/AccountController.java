package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/add")
    public @ResponseBody String addNewAccount(@RequestParam String accountNumber, @RequestParam Account.AccountType accountType, @RequestParam int clientId) {
        // Client fetching and validation will be handled in the ClientController - to be created
        Account account = new Account(accountNumber, accountType, new Client());
        accountRepository.save(account);
        return "Account created successfully";
    }

    
}
