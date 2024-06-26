package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ViewController {

    @Autowired
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private LoanRepository loanRepository;

    @RequestMapping("/")
    public String viewStartPage() {
        return "redirect:/welcome";
    }

    @RequestMapping("/welcome")
    public String viewWelcomePage() {
        return "indexTemplates/welcome";
    }

    @GetMapping("/hello")
    public String viewHelloPage(HttpServletRequest request, Model model) {
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        model.addAttribute("accNumber", client.getCheckingAccount().getAccountNumber());
        model.addAttribute("balance", client.getCheckingAccount().getAccountBalance());
        String date = LocalDateTime.now().getDayOfMonth() + " " + LocalDateTime.now().getMonth() + " " + LocalDateTime.now().getYear();
        model.addAttribute("date", date);
        return "indexTemplates/hello";
    }

    @GetMapping("/admin")
    public String viewAdminPanel(HttpServletRequest request, Model model){
        Client requester = detailsOfLoggedClient.getLoggedClientInstance(request);
        model.addAttribute("accNumber", requester.getCheckingAccount().getAccountNumber());
        model.addAttribute("balance", requester.getCheckingAccount().getAccountBalance());
        String date = LocalDateTime.now().getDayOfMonth() + " " + LocalDateTime.now().getMonth() + " " + LocalDateTime.now().getYear();
        model.addAttribute("date", date);
        model.addAttribute("adminInfo", "Welcome to admin panel!");
        if(requester.isAdmin()){
            return "adminTemplates/adminPanel";
        }else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
    }

    @GetMapping("/makeTransaction")
    public String showTransactionForm(HttpServletRequest request, Model model){
        Client requester = detailsOfLoggedClient.getLoggedClientInstance(request);
        model.addAttribute("clientAccount", requester.getCheckingAccount().getAccountNumber());
        return "transactionTemplates/makeTransactionForm";
    }

    @GetMapping("/withdrawDeposit")
    public String showWithdrawDepositForm(HttpServletRequest request, Model model){
        Client requester = detailsOfLoggedClient.getLoggedClientInstance(request);
        List<Deposit> deposits = depositRepository.getAllByClient(requester);

        ArrayList<Deposit> activeDeposits = new ArrayList<>();
        for(Deposit x : deposits){
            if(x.getIsActive()){
                activeDeposits.add(x);
            }else{
                continue;
            }
        }
        if(activeDeposits.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have any active deposits");
        }
        model.addAttribute("availableDeposits", activeDeposits);
        return "depositTemplates/withdrawForm";
    }

    @GetMapping("/nextInstallment")
    public String showNextInstallmentForm(HttpServletRequest request, Model model){
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        List<Loan> listOfLoans = loanRepository.findAll();
        ArrayList<Loan> loansOfClient = new ArrayList<>();
        for(Loan x : listOfLoans){
            if(x.getClient() == client){
                loansOfClient.add(x);
            }
        }
        if(loansOfClient.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have any loans!");
        }
        model.addAttribute("ClientLoans", loansOfClient);
        return "installmentTemplates/nextForm";
    }

}
