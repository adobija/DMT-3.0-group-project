package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.AccountService;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/loan")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @PostMapping("/add")
    public @ResponseBody String addNewLoan(@RequestParam double principalAmount,
                                           @RequestParam double interestRate,
                                           @RequestParam double commisionRate,
                                           @RequestParam int loanDuration,
                                           @RequestParam String bankAccountNumber,
                                           HttpServletRequest request) {
        String currentName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(currentName);
        Account checkingAccount = client.getCheckingAccount();
        if (checkingAccount == null) {
            return "Checking account has not been found";
        }

        Account bankAccount = accountRepository.findByAccountNumber(bankAccountNumber);
        if (bankAccount == null) {
            return "Bank account has not been found";
        }

        // Create a new loan account using the AccountService's method
        String response = accountService.addNewAccount(Account.AccountType.LOAN, client);
        if (!response.contains("Account created successfully")) {
            return response;
        }

        // Fetch the newly created loan account
        Account loanAccount = accountService.getLatestAccount();

        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);

        // Calculating commision and amount of intrests
        double intrestForBank = loan.intrestAmount(principalAmount, interestRate, loanDuration);
        double commisionForBank = loan.commisionAmout(principalAmount, commisionRate);
        // Setting date of loan
        loan.setDateOfLoan(LocalDateTime.now());
        // Money transfer from the account where loan is launched to the checking
        // account of the customer
        Transaction t1 = new Transaction(loanAccount, checkingAccount, principalAmount);
        transactionRepository.save(t1);
        // Profit from interest transfer from the account where loan is launched to the
        // bank's account
        Transaction t2 = new Transaction(loanAccount, bankAccount, intrestForBank);
        transactionRepository.save(t2);
        // Profit from commision transfer from the account where loan is launched to the
        // bank's account
        Transaction t3 = new Transaction(loanAccount, bankAccount, commisionForBank);
        transactionRepository.save(t3);
        // Updating total amount of the loan
        double totalLoanAmount = principalAmount + intrestForBank + commisionForBank;
        loan.setTotalLoanAmout(totalLoanAmount);
        loan.setLeftToPay(totalLoanAmount);
        // Generating installments
        loan.generateInstallments();
        // Setting loan to active
        loan.setIsActive(true);

        loanRepository.save(loan);
        loanAccount.setLoan(loan);
        accountRepository.save(loanAccount);

        return "Loan and loan account created successfully";
    }

    @GetMapping("/all")
    public @ResponseBody List<Loan> getAllLoans(HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        return loanRepository.findAll();
    }

    @GetMapping("/byLoanId")
    public @ResponseBody Loan getLoanById(@RequestParam int loanID, HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        Optional<Loan> loanOptional = loanRepository.findById(loanID);
        return loanOptional.orElse(null);
    }
}
