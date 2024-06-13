package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/loan")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/add")
    public @ResponseBody String addNewLoan(@RequestParam String loanAccountNumber,
                                           @RequestParam String checkingAccountNumber,
                                           @RequestParam double principalAmount,
                                           @RequestParam double interestRate,
                                           @RequestParam double commisionRate,
                                           @RequestParam int loanDuration,
                                           @RequestParam String bankAccountNumber) {
        Account loanAccount = accountRepository.findByAccountNumber(loanAccountNumber);
        Account checkingAccount = accountRepository.findByAccountNumber(checkingAccountNumber);
        Account bankAccount = accountRepository.findByAccountNumber(bankAccountNumber);

        if (loanAccount == null || checkingAccount == null || bankAccount == null) {
            return "One or more accounts not found";
        }

        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        loanRepository.save(loan);
        return "Loan created successfully";
    }

    @GetMapping("/all")
    public @ResponseBody List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @GetMapping("/byLoanId")
    public @ResponseBody Loan getLoanById(@RequestParam int loanID) {
        Optional<Loan> loanOptional = loanRepository.findById(loanID);
        return loanOptional.orElse(null);
    }
}
