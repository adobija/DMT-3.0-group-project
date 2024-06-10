package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

}
