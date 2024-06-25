package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class ViewController {

    @Autowired
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @Autowired
    private DepositRepository depositRepository;

    @RequestMapping("/")
    public String viewStartPage() {
        return "redirect:/welcome";
    }

    @RequestMapping("/welcome")
    public String viewWelcomePage() {
        return "indexTemplates/welcome";
    }

    @GetMapping("/hello")
    public String viewHelloPage() {
        return "indexTemplates/hello";
    }

    @GetMapping("/admin")
    public String viewAdminPanel(HttpServletRequest request){
        Client requester = detailsOfLoggedClient.getLoggedClientInstance(request);
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
        if(deposits.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have any deposits");
        }
        model.addAttribute("avaibleDeposits", deposits);
        return "depositTemplates/withdrawForm";
    }

}
