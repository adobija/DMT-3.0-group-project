package com.dmt.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/installment")
public class InstallmentController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @GetMapping("/my")
    public @ResponseBody String getMyInstallments(HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        List<Loan> loans = client.getLoansList();
        StringBuilder output = new StringBuilder();
        
        for (Loan loan : loans) {
            List<Installment> installments = loan.getInstallments();
            for (Installment installment: installments) {
                output.append("Loan ")
                        .append(loan.getLoanID())
                        .append(": installment ")
                        .append(installment.getInstallmentID())
                        .append("  amount: ")
                        .append(installment.getInstallmentAmount())
                        .append("  already paid: ")
                        .append(installment.getPaidAmount())
                        .append("  is settled: ")
                        .append(installment.getIsPaid())
                        .append("\n");
            }
        }
        
        return output.toString();
    }
}