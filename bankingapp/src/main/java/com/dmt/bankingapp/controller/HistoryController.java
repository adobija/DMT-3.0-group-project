package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/history")
public class HistoryController {

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClientImp;

    public HistoryController(DetailsOfLoggedClientImpl detailsOfLoggedClientImp) {
        this.detailsOfLoggedClientImp = detailsOfLoggedClientImp;
    }
}
