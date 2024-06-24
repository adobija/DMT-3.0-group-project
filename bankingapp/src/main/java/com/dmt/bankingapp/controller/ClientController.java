package com.dmt.bankingapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @PostMapping("/editName")
    public String editName(@RequestParam String newName, HttpServletRequest request, Model model) {
        if (clientRepository.findByClientName(newName) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Name has already been used - failed to update client's name");
        }
        String currentName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(currentName);
        if (client != null) {
            client.setClientName(newName);
            clientRepository.save(client);
            model.addAttribute("response","Client's name updated successfully - RELOGIN TO COMMIT CHANGES");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
        return "indexTemplates/hello";
    }

    @PostMapping("/editPassword")
    public String editPassword(@RequestParam String oldPassword, @RequestParam String newPassword, HttpServletRequest request, Model model) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        if (client != null) {
            String storedHashedPassword = client.getClientPassword().replace("{bcrypt}", "");// Remove prefix for comparison
            if(!BCrypt.checkpw(oldPassword, storedHashedPassword)){
                throw new ResponseStatusException(HttpStatus.LOCKED, "Please input valid old password!");
            }
            if (BCrypt.checkpw(newPassword, storedHashedPassword)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Please choose a new password that is different from the previous password");
            } else {
                client.setClientPassword(newPassword);
                clientRepository.save(client);
                model.addAttribute("response", "Client's password updated successfully - RELOGIN TO COMMIT CHANGES");
                return "indexTemplates/hello";
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @PostMapping("/editAdmin")
    public String editPermission(@RequestParam int clientId, @RequestParam boolean isAdmin, HttpServletRequest request, Model model) throws IOException {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        Optional<Client> foundClientOptional = clientRepository.findById(clientId);
        Client foundClient = foundClientOptional.get();
        if (foundClient != null) {
            foundClient.setAdmin(isAdmin);
            clientRepository.save(foundClient);
            model.addAttribute("response","Client's permission updated successfully");
            return "indexTemplates/hello";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    @GetMapping("/all")
    public String getAllClients(HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        List<Client> listOfClients = clientRepository.findAll();
        model.addAttribute("listOfClients", listOfClients);
        return "clientTemplates/allClients";
    }

    @GetMapping("/byClientID")
    public String getByClientID(@RequestParam int clientID, HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if(!requester.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        Client client = clientRepository.findByClientID(clientID);

        model.addAttribute("imieKlienta", client.getClientName());
        model.addAttribute("admin", client.isAdmin());
        model.addAttribute("haslo", client.getClientPassword());
        return "testTemplates/test";
    }

    @GetMapping("/checkingBalance")
    public @ResponseBody double getCheckingBalance(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        return client.getCheckingAccount().getAccountBalance();
    }

    @GetMapping("/depositsBalance")
    public @ResponseBody String getDepositsBalance(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        List<Deposit> deposits = client.getDepositsList();
        StringBuilder depositsBalance = new StringBuilder();
        
        for (Deposit deposit : deposits) {
            depositsBalance.append(deposit.getDepositID())
                        .append(": ")
                        .append(deposit.getTotalDepositAmount())
                        .append("\n");
        }
        
        // Remove the last newline in the output
        if (depositsBalance.length() > 0) {
            depositsBalance.setLength(depositsBalance.length() - 1);
        }
        
        return depositsBalance.toString();
    }

    @GetMapping("/loansBalance")
    public @ResponseBody String getLoansBalance(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        List<Loan> loans = client.getLoansList();
        StringBuilder loansBalance = new StringBuilder();

        double total = 0;
        
        for (Loan loan : loans) {
            total += loan.getLeftToPay();
            loansBalance.append(loan.getLoanID())
                        .append(": ")
                        .append(loan.getLeftToPay())
                        .append("\n");
        }
        loansBalance.append("Remaining total amount of loans: " + total);
        
        return loansBalance.toString();
    }

}
