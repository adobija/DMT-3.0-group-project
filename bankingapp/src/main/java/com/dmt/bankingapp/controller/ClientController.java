package com.dmt.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @PostMapping("/add")  // curl.exe -d "clientName=NAME&clientPassword=PASSWORD&isAdmin=false" http://localhost:8080/client/add
    public @ResponseBody String addNewClient(@RequestParam String clientName, @RequestParam String clientPassword, @RequestParam boolean isAdmin) {
        Client exists = clientRepository.findByClientName(clientName);
        if (exists != null) {
            return "Client with his user name already exists. Failed to create new client profile!";
        } else {
            Client client = new Client(clientName, isAdmin, clientPassword);
            clientRepository.save(client);
            return "New client profile created successfully";
        }
    }

    @PostMapping("/editName")
    public @ResponseBody String editName(@RequestParam String newName, HttpServletRequest request) {
        String currentName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(currentName);
        if (client != null) {
            client.setClientName(newName);
            clientRepository.save(client);
            return "Client's name updated successfully";
        } else {
            return "Client not found";
        }
    }

    @PostMapping("/editPassword")
    public @ResponseBody String editPassword(@RequestParam String newPassword, HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        if (client != null) {
            client.setClientPassword(newPassword);
            clientRepository.save(client);
            return "Client's password updated successfully";
        } else {
            return "Client not found";
        }
    }

    @PostMapping("/editAdmin")
    public @ResponseBody String editPermission(@RequestParam boolean isAdmin, HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        if (client != null) {
            client.setAdmin(isAdmin);
            clientRepository.save(client);
            return "Client's permission updated successfully";
        } else {
            return "Client not found";
        }
    }

    @GetMapping("/all")
    public @ResponseBody List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/byClientID")
    public @ResponseBody Client getByClientID(@RequestParam int clientID) {
        return clientRepository.findByClientID(clientID);
    }
}
