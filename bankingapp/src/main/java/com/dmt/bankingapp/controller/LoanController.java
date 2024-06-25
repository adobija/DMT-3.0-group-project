package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.*;
import com.dmt.bankingapp.repository.*;
import com.dmt.bankingapp.service.AccountService;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
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

    @Autowired
    private CommissionRepository commissionRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/add")
    public String addNewLoan(@RequestParam double principalAmount,
            @RequestParam int loanDuration,
            HttpServletRequest request,
            Model model) {
        String currentName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(currentName);
        Account checkingAccount = client.getCheckingAccount();
        if (checkingAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account has not been found");
        }

        Account bankAccount = accountRepository.findByAccountNumber("BANK_LOAN");
        if (bankAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank account has not been found");
        }

        // Create a new loan account using the AccountService's method
        String response = accountService.addNewAccount(Account.AccountType.LOAN, client);
        if (!response.contains("Account created successfully")) {
            return response;
        }

        // Fetch the newly created loan account
        Account loanAccount = accountService.getLatestAccount();

        // Fetch live commission rate
        double commisionRate = commissionRepository.findByCommissionOf("LOAN_COMMISSION").getCommissionRateInPercent();

        // Fetch live interest rate
        double interestRate = commissionRepository.findByCommissionOf("LOAN_INTEREST").getCommissionRateInPercent();

        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration,
                bankAccount);

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

        String output = "Loan and loan account created successfully";
        model.addAttribute("add", output);
        return "loanTemplates/add";
    }

    @GetMapping("/all")
    public String getAllLoans(HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        List<Loan> allLoans = loanRepository.findAll();
        StringBuilder output = new StringBuilder();

        for (Loan loan : allLoans) {
            LocalDateTime dateOfLoan = loan.getDateOfLoan();
            String formattedDate = dateOfLoan.format(formatter);

            output.append(loan.getLoanID())
                    .append(": ")
                    .append(formattedDate)
                    .append("  account: ")
                    .append(loan.getLoanAccount().getAccountNumber())
                    .append("  total: ")
                    .append(loan.getTotalLoanAmount())
                    .append("  left: ")
                    .append(loan.getLeftToPay())
                    .append("  client: ")
                    .append(loan.getClient().getClientID())
                    .append("\n");
        }

        // Remove the last newline in the output
        if (output.length() > 0) {
            output.setLength(output.length() - 1);
        }
        model.addAttribute("all", output.toString());
        return "loanTemplates/allLoans";
    }

    @GetMapping("/byLoanId")
    public String getLoanById(@RequestParam int loanID, HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        Loan loan = loanRepository.findByLoanID(loanID);
        if (loan == null) {
            throw new NoSuchElementException("Loan with this ID does not exist!");
        }

        StringBuilder output = new StringBuilder();
        LocalDateTime dateOfLoan = loan.getDateOfLoan();
        String formattedDate = dateOfLoan.format(formatter);

        output.append("loan ID: ")
                .append(loan.getLoanID())
                .append("\ndate: ")
                .append(formattedDate)
                .append("\naccount: ")
                .append(loan.getLoanAccount().getAccountNumber())
                .append("\ntotal: ")
                .append(loan.getTotalLoanAmount())
                .append("\nleft: ")
                .append(loan.getLeftToPay())
                .append("\nclient: ")
                .append(loan.getClient().getClientID())
                .append("\nduration: ")
                .append(loan.getLoanDuration())
                .append("\ninterest: ")
                .append(loan.getInterestRate())
                .append("\ncommision: ")
                .append(loan.getCommisionRate())
                .append("\nactive: ")
                .append(loan.getIsActive());
        model.addAttribute("loanbyid", output.toString());
        return "loanTemplates/loanbyid";
    }
}