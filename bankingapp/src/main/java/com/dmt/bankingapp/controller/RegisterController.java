package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/register")
public class RegisterController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @PostMapping("/newClient")  // curl.exe -d "clientName=NAME&clientPassword=PASSWORD&isAdmin=false" http://localhost:8080/register/newClient
    public @ResponseBody String addNewClient(@RequestParam String clientName, @RequestParam String clientPassword) {
        Client exists = clientRepository.findByClientName(clientName);
        if (exists != null) {
            return "Client with this user name already exists - failed to create new client profile";
        } else {
            Client client = new Client(clientName, false, clientPassword);
            clientRepository.save(client);
            accountService.addNewAccount(Account.AccountType.CHECKING, client);
            client.addAccount(accountService.getLatestAccount());
            client.setCheckingAccount(accountService.getLatestAccount());
            clientRepository.save(client);
            return "New client profile created successfully";
        }
    }
}
