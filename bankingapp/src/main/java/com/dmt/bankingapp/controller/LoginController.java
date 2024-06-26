package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.service.implementation.RedirectLoggedClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Autowired
    private final RedirectLoggedClientImpl redirectLoggedClient;

    public LoginController(RedirectLoggedClientImpl redirectLoggedClient) {
        this.redirectLoggedClient = redirectLoggedClient;
    }

    @GetMapping("/login")
    public String viewLoginPage() {
        if(redirectLoggedClient.isAuthenticated()){
            return "redirect:/hello";
        }
        return "loginTemplates/login";
    }
}
