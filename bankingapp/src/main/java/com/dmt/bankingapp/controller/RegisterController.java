package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping("/newClient")
    public @ResponseBody String addNewClient(@RequestParam String clientName, @RequestParam String clientPassword, Model model) {
        Client exists = clientRepository.findByClientName(clientName);
        if (exists != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client with this user name already exists - failed to create new client profile");
        } else {
            Client client = new Client(clientName, false, clientPassword);
            clientRepository.save(client);
            accountService.addNewAccount(Account.AccountType.CHECKING, client);
            client.addAccount(accountService.getLatestAccount());
            client.setCheckingAccount(accountService.getLatestAccount());
            clientRepository.save(client);
            model.addAttribute("return", "New client profile created successfully");
            return "New client profile created successfully";
        }
    }
}
