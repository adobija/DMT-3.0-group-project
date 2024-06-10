package com.dmt.bankingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/add")
    public @ResponseBody String addNewClient(@RequestParam String clientName, @RequestParam String clientPassword, @RequestParam boolean isAdmin) {
        Client client = new Client(clientName, isAdmin, clientPassword);
        clientRepository.save(client);
        return "Account created successfully";
    }

    @PostMapping("/edit")
    public @ResponseBody String editClient(@RequestParam int clientId, @RequestParam String clientName, @RequestParam String clientPassword, @RequestParam boolean isAdmin) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client != null) {
            client.setClientName(clientName);
            client.setClientPassword(clientPassword);
            client.setAdmin(isAdmin);
            clientRepository.save(client);
            return "Account balance updated successfully";
        } else {
            return "Account not found";
        }
    }
}
