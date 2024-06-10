package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping(path = "/history")
public class HistoryController {

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClientImp;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    public HistoryController(DetailsOfLoggedClientImpl detailsOfLoggedClientImp, ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.detailsOfLoggedClientImp = detailsOfLoggedClientImp;
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    //Endpoint for history of checking account
    @GetMapping(path = "/checking")
    public @ResponseBody String getHistoryOfIncomeAndExpense(HttpServletRequest request){
//        //Get name of logged client
//        String login = detailsOfLoggedClientImp.getNameFromClient(request);
//        //find instance of this client
//        Client client = clientRepository.findByClientName(login);
//        //get ID of client
//        Integer clientID = client.getClientID();
//        //find instance of client's account
//        Account account = accountRepository.findByClientId(clientID);
//        //get ID of account
//        Integer accountId = account.getAccountID();
//        //fetch all records in Transaction table that have ID of account
//        List<Transaction> incomeTransactions = transactionRepository.findByAccountOfReceiver(accountId);
//        List<Transaction> expenseTransactions = transactionRepository.findByAccountOfSender(accountId);
//
//        //return list of transactions
//        return incomeTransactions;


        return "test";
    }
}
