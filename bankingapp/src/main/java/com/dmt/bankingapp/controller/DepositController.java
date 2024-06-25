package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Deposit.DepositType;
import com.dmt.bankingapp.entity.Transaction;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
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
        if (checkingAccount.getAccountBalance() < totalDepositAmount) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You don't have enough money!");
        }
        Account bankAccount = accountRepository.findByAccountNumber("BANK_DEPOSIT");

        DepositType depositTypeValue = null;

        if (depositType.equalsIgnoreCase("FIXED")) {
            depositTypeValue = DepositType.FIXED;
        }

        else if (depositType.equalsIgnoreCase("PROGRESSIVE")) {
            depositTypeValue = DepositType.PROGRESSIVE;
        }
        // Check if client have already this type of Deposit
        List<Deposit> checkDeposits = depositRepository.getAllByClient(client);
        for (Deposit x : checkDeposits) {
            if (x.getDepositType().equals(depositTypeValue) && x.getIsActive()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                        "Client have already one deposit of type " + depositTypeValue + "!");
            }
        }

        // Fetch live commision of deposit
        double commissionRate = commissionRepository.findByCommissionOf("DEPOSIT").getCommissionRateInPercent();

        Deposit deposit = new Deposit(commissionRate, depositDuration, checkingAccount, totalDepositAmount,
                depositTypeValue);

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

    @GetMapping("/withdraw")
    public String withdrawDeposit(HttpServletRequest request, String depositType, Model model) {
        // Get client instance
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        // Fetch his deposit records
        List<Deposit> depositsOfClient = depositRepository.getAllByClient(client);
        // error if client don't have any deposits
        if (depositsOfClient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not have any deposits!");
        }

        // check what type
        DepositType type = null;
        if (depositType.equalsIgnoreCase("FIXED")) {
            type = DepositType.FIXED;
        } else if (depositType.equalsIgnoreCase("PROGRESSIVE")) {
            type = DepositType.PROGRESSIVE;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no type of deposit as " + depositType.toUpperCase() + "!");
        }

        // Get instance of this deposit
        Deposit requestedDeposit = null;
        for (Deposit x : depositsOfClient) {
            if (x.getDepositType().equals(type)) {
                requestedDeposit = x;
            }
        }
        if (requestedDeposit == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Deposit has not been found!");
        }

        // Test if client can withdraw deposit
        if (!requestedDeposit.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot transfer money from this deposit account because the deposit has already been withdrawn! Date of withdrawn: " + requestedDeposit.getDateOfWithdrawn());
        }

        // Fetch account of bank
        Account bankAccount = accountRepository.findByAccountNumber("BANK_DEPOSIT");

        // Get client checking account
        Account clientCheckingAccount = client.getCheckingAccount();

        // Deposit withdraw
        LocalDateTime expectedDateOfWithdraw = requestedDeposit.getDateOfDeposit()
                .plusMonths(requestedDeposit.getDepositDuration());
        if (expectedDateOfWithdraw.isBefore(LocalDateTime.now())
                || expectedDateOfWithdraw.isEqual(LocalDateTime.now())) {
            Transaction withdraw = new Transaction(bankAccount, clientCheckingAccount,
                    requestedDeposit.getReturnOfInvestment());
            transactionRepository.save(withdraw);
            requestedDeposit.setActive(false);
            requestedDeposit.setDateOfWithdrawn(LocalDateTime.now());
            depositRepository.save(requestedDeposit);
        } else {
            throw new ResponseStatusException(HttpStatus.LOCKED, "You cannot withdraw money from that deposit until " + expectedDateOfWithdraw + "!");
        }

        String output = "Successfully withdrawn " + requestedDeposit.getReturnOfInvestment();
        model.addAttribute("withdraw", output);
        return "depositTemplates/withdraw";
    }
}
