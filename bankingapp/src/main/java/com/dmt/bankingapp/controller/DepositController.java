package com.dmt.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Deposit.DepositType;
import com.dmt.bankingapp.entity.Transaction;

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

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @PostMapping("/addNewDeposit")
    public @ResponseBody String addNewDeposit(

            @RequestParam double totalDepositAmount,
            @RequestParam int depositDuration,
            @RequestParam String depositType,
            HttpServletRequest request) {

        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);

        List<Account> checkingAccount = accountRepository.findByClient(client);
        
        Account foundAccount = null;
        for (Account account : checkingAccount) {
            if (account.getAccountType().equals(Account.AccountType.CHECKING)) {
                foundAccount = account;
                break;
            }
        }
        if (foundAccount == null) {
            return "You don't have a checking account";
        }

        Account bankAccount = accountRepository.findByAccountNumber("BANK_DEPOSIT");

        DepositType depositTypeValue = null;

        if (depositType.equalsIgnoreCase("FIXED")) {

            depositTypeValue = DepositType.FIXED;
        }

        else if (depositType.equalsIgnoreCase("PROGRESSIVE")) {
            depositTypeValue = DepositType.PROGRESSIVE;
        } 
        

        Deposit deposit = new Deposit(10, depositDuration, foundAccount, totalDepositAmount, depositTypeValue);
        
        switch (deposit.getDepositType()) {

            case FIXED:
                deposit.calculateFixedTermDeposit();
            break;

            case PROGRESSIVE:
                deposit.calculateFixedTermDeposit();
            break;
        }

        Transaction t1 = new Transaction(foundAccount, bankAccount, totalDepositAmount);
        transactionRepository.save(t1);

        depositRepository.save(deposit);

        return "Deposit added successfully";
    }

}
