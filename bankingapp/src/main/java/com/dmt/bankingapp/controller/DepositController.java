package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Deposit.DepositType;
import com.dmt.bankingapp.entity.Transaction;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

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
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @Autowired
    private CommissionRepository commissionRepository;

    @PostMapping("/addNewDeposit")
    public @ResponseBody String addNewDeposit(

            @RequestParam double totalDepositAmount,
            @RequestParam int depositDuration,
            @RequestParam String depositType,
            HttpServletRequest request) {

        String currentName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(currentName);
        Account checkingAccount = client.getCheckingAccount();
        if (checkingAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account has not been found");
        }

        Account bankAccount = accountRepository.findByAccountNumber("BANK_DEPOSIT");

        DepositType depositTypeValue = null;

        if (depositType.equalsIgnoreCase("FIXED")) {
            depositTypeValue = DepositType.FIXED;
        }

        else if (depositType.equalsIgnoreCase("PROGRESSIVE")) {
            depositTypeValue = DepositType.PROGRESSIVE;
        } 

        // Fetch live commision of deposit
        int commissionRate = commissionRepository.findByCommissionOf("DEPOSIT").getCommissionRateInPercent();

        Deposit deposit = new Deposit(commissionRate, depositDuration, checkingAccount, totalDepositAmount, depositTypeValue);
        
        switch (deposit.getDepositType()) {
            case FIXED:
                deposit.calculateFixedTermDeposit();
            break;

            case PROGRESSIVE:
                deposit.calculateProgressiveDeposit();
            break;
        }

        Transaction t1 = new Transaction(checkingAccount, bankAccount, totalDepositAmount);
        transactionRepository.save(t1);

        depositRepository.save(deposit);

        return "Deposit added successfully";
    }

}
