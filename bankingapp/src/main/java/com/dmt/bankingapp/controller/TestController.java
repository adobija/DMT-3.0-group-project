package com.dmt.bankingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/")
    public String viewStartPage() {
        return "redirect:/welcome";
    }

    @RequestMapping("/welcome")
    public String viewWelcomePage() {
        return "welcome";
    }

    @RequestMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/hello")
    public String viewHelloPage() {
        return "hello";
    }

    @GetMapping("/someEndpoint")
    public String someEndpoint() {
        return "Success";
    }
}