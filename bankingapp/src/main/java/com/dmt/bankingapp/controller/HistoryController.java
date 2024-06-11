package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import com.dmt.bankingapp.record.History;
import com.dmt.bankingapp.utils.HistoryRecordGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
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
    public @ResponseBody ArrayList<History> getHistoryOfIncomeAndExpense(HttpServletRequest request){
        //Get name of logged client
        String login = detailsOfLoggedClientImp.getNameFromClient(request);
        //find instance of this client
        Client client = clientRepository.findByClientName(login);
        //find instance of client's account
        Account account = accountRepository.findByClient(client);
        //fetch all records in Transaction table that have ID of account
        //All transaction where found account was receiver
        List<Transaction> incomeTransactions = transactionRepository.findByReceiver(account);
        //All transaction where found account was giver
        List<Transaction> expenseTransactions = transactionRepository.findByGiver(account);
        //Array list of history instance records
        ArrayList<History> historyArrayList = new ArrayList<>();

        for(Transaction x : incomeTransactions){
            //for each loop to convert and add data from list of transactions to readable history records
            History historyRecord = HistoryRecordGenerator.castDataToCreateHistoryRecord(x,account);
            historyArrayList.add(historyRecord);
        }
        for(Transaction x : expenseTransactions){
            //for each loop to convert and add data from list of transactions to readable history records
            History historyRecord = HistoryRecordGenerator.castDataToCreateHistoryRecord(x,account);
            historyArrayList.add(historyRecord);
        }
        //Sort history to chronological order by date of transaction
        historyArrayList.sort(Comparator.comparing(History::dateOfTransaction));


        //return list of transactions as History records
        return historyArrayList;
    }

    @GetMapping(path = "/checking/{accountNumber}")
    public void getHistoryOfAccountByAccountNumberAdminOnly(@PathVariable String accountNumber){
        //todo: finish this method
        //todo: create tests for methods in each repository
        //todo:
    }
}
