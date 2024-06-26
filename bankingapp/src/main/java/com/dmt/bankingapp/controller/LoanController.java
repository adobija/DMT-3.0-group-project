package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.*;
import com.dmt.bankingapp.record.deposit.DepositRecord;
import com.dmt.bankingapp.record.loans.AllLoansRecord;
import com.dmt.bankingapp.record.loans.LoanRecord;
import com.dmt.bankingapp.repository.*;
import com.dmt.bankingapp.service.AccountService;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        model.addAttribute("response", output);
        return "indexTemplates/hello";
    }

    @GetMapping("/all")
    public String getAllLoans(HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        List<Loan> allLoans = loanRepository.findAll();


        ArrayList<AllLoansRecord> allLoansRecords = new ArrayList<>();

        for (Loan loan : allLoans) {
            LocalDateTime dateOfLoan = loan.getDateOfLoan();
            String formattedDate = dateOfLoan.format(formatter);

            allLoansRecords.add(new AllLoansRecord(loan.getLoanID(), formattedDate, loan.getLoanAccount().getAccountNumber(), loan.getTotalLoanAmount(), loan.getLeftToPay(), loan.getClient().getClientID()));
        }
        Collections.sort(allLoansRecords, new Comparator<AllLoansRecord>() {
            @Override
            public int compare(AllLoansRecord loan1, AllLoansRecord loan2) {
                return Integer.compare(loan2.loanID(), loan1.loanID());
            }
        });
        model.addAttribute("all", allLoansRecords);
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

        LocalDateTime dateOfLoan = loan.getDateOfLoan();
        String formattedDate = dateOfLoan.format(formatter);
        LoanRecord loanRecord = new LoanRecord(loan.getLoanID(), formattedDate, loan.getLoanAccount().getAccountNumber(), loan.getTotalLoanAmount(), loan.getLeftToPay(), loan.getClient().getClientID(), loan.getLoanDuration(), loan.getInterestRate(), loan.getCommisionRate(), loan.getIsActive());

        model.addAttribute("loanbyid", loanRecord);
        return "loanTemplates/loanbyid";
    }
}