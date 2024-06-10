package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/history")
public class HistoryController {

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClientImp;

    public HistoryController(DetailsOfLoggedClientImpl detailsOfLoggedClientImp) {
        this.detailsOfLoggedClientImp = detailsOfLoggedClientImp;
    }
    //Endpoint for history of checking account
    @GetMapping(path = "/checking")
    public @ResponseBody List<Transaction> getHistoryOfIncomeAndExpense(HttpServlet request){
        //Get name of logged client
        //find all details of this client
        //get ID of client
        //get account of client
        //find all details of client's account
        //get ID of account
        //fetch all records in Transaction table that have ID of account
    }
}
