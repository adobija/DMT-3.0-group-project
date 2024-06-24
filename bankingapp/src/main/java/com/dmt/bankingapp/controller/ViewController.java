package com.dmt.bankingapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ViewController {
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

}
