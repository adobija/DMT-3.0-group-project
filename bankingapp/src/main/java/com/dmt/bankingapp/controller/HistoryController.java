package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.implementation.AccountsOfUserIntoHistoryListImpl;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import com.dmt.bankingapp.record.History;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping(path = "/history")
public class HistoryController {

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClientImp;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final AccountsOfUserIntoHistoryListImpl accountsOfUserIntoHistoryList;

    public HistoryController(DetailsOfLoggedClientImpl detailsOfLoggedClientImp, ClientRepository clientRepository, AccountsOfUserIntoHistoryListImpl accountsOfUserIntoHistoryList) {
        this.detailsOfLoggedClientImp = detailsOfLoggedClientImp;
        this.clientRepository = clientRepository;
        this.accountsOfUserIntoHistoryList = accountsOfUserIntoHistoryList;
    }
    //Endpoint for history of checking account
    @GetMapping(path = "/checking")
    public @ResponseBody  ResponseEntity<ArrayList<History>> getHistoryOfIncomeAndExpense(HttpServletRequest request){
        //Get instance of logged client
        Client client = detailsOfLoggedClientImp.getLoggedClientInstance(request);
        //Call method to get history of all accounts associated to client
        ResponseEntity<ArrayList<History>> historyArrayList = accountsOfUserIntoHistoryList.getStoredHistoryByClient(client);
        //return list of transactions as History records
        return historyArrayList;
    }

    @GetMapping(path = "/checking/{clientId}")
    public @ResponseBody ResponseEntity<ArrayList<History>> getHistoryOfAccountByAccountNumberAdminOnly(@PathVariable int clientId, HttpServletRequest request){
        //check if accessing account is admin or throw exception
        Client loggedClient = detailsOfLoggedClientImp.getLoggedClientInstance(request);
        if(!loggedClient.isAdmin()){
            HttpHeaders header = new HttpHeaders();
            header.add("ErrorMessage", "You don't have enough permission to perform this action!");
            return new ResponseEntity<>(null, header, HttpStatus.FORBIDDEN);
        }
        //Find client instance by id
        Optional<Client> client = clientRepository.findById(clientId);
        if(client.isEmpty()){
            HttpHeaders header = new HttpHeaders();
            header.add("ErrorMessage", "Client with id "+clientId+" does not exist!");
            return new ResponseEntity<>(null, header, HttpStatus.NOT_FOUND);
        }
        Client foundClient = client.get();

        //Call method to get history of all accounts associated to client
        ResponseEntity<ArrayList<History>> historyArrayList = accountsOfUserIntoHistoryList.getStoredHistoryByClient(foundClient);
        //return list of transactions as History records
        return historyArrayList;
    }
}
