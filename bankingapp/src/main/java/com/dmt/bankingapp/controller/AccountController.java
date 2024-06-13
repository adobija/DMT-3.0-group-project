package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")
    public @ResponseBody String addNewAccount(@RequestParam Account.AccountType accountType, @RequestParam Client client) {
        return accountService.addNewAccount(accountType, client);
    }

    @GetMapping("/all")
    public @ResponseBody List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/byAccountNumber")
    public @ResponseBody Account getAccountByNumber(@RequestParam String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }
}
