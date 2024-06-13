package com.dmt.bankingapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.AccountService;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @Autowired
    private AccountService accountService;

    @PostMapping("/add")  // curl.exe -d "clientName=NAME&clientPassword=PASSWORD&isAdmin=false" http://localhost:8080/client/add
    public @ResponseBody String addNewClient(@RequestParam String clientName, @RequestParam String clientPassword, @RequestParam boolean isAdmin) {
        Client exists = clientRepository.findByClientName(clientName);
        if (exists != null) {
            return "Client with his user name already exists. Failed to create new client profile!";
        } else {
            Client client = new Client(clientName, isAdmin, clientPassword);
            clientRepository.save(client);
            accountService.addNewAccount(Account.AccountType.CHECKING, client.getClientID(), client);
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
            String storedHashedPassword = client.getClientPassword().replace("{bcrypt}", ""); // Remove prefix for comparison
            if (BCrypt.checkpw(newPassword, storedHashedPassword)) {
                return "Please choose a new password that is different from the previous password";
            } else {
                client.setClientPassword(newPassword);
                clientRepository.save(client);
                return "Client's password updated successfully";
            }
        } else {
            return "Client not found";
        }
    }

    @PostMapping("/editAdmin/{clientId}")
    public @ResponseBody String editPermission(@PathVariable int clientId, @RequestParam boolean isAdmin, HttpServletRequest request) throws IOException {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        if(!client.isAdmin()){
            return "You don't have permission!";
        }
        Optional<Client> foundClientOptional = clientRepository.findById(clientId);
        Client foundClient = foundClientOptional.get();
        if (foundClient != null) {
            foundClient.setAdmin(isAdmin);
            clientRepository.save(foundClient);
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
