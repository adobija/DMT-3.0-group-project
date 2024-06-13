package com.dmt.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.repository.TransactionRepository;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(path = "/deposit")
public class DepositController {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/addNewDeposit")
    public @ResponseBody String addNewDeposit(@RequestParam String checkingAccountNumber,
            @RequestParam double depositAmount,
            @RequestParam String bankAccountNumber,
            @RequestParam String depositType,
            @RequestParam String depositDuration,
            @RequestParam String depositInterestRate) {

        Account checkingAccount = accountRepository.findByAccountNumber(checkingAccountNumber);
        Account bankAccount = accountRepository.findByAccountNumber(bankAccountNumber);

        if (checkingAccount == null || bankAccount == null) {
            return "One or more accounts not found";
        }

        Client client = checkingAccount.getClient();
        if (client == null) {
            return "Client not found";
        }

        return "Deposit added successfully";
    }

}
