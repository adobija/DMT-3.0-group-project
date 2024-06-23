package com.dmt.bankingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.InstallmentRepository;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;
import com.dmt.bankingapp.utils.DateAdjuster;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/installment")
public class InstallmentController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

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
                        .append("  due date: ")
                        .append(DateAdjuster.getDate(installment.getDueDate()))
                        .append("\n");
            }
        }
        
        return output.toString();
    }

    @GetMapping("/next")
    public @ResponseBody String getNextInstallment(int loanId, HttpServletRequest request) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        Loan loan = loanRepository.findByLoanID(loanId);

        if (loan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan has not been found");
        }

        if (!client.equals(loan.getClient())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        if (!loan.getIsActive()) {
            return "Loan has been paid - there is no installments left to pay";
        }

        List<Installment> installments = loan.getInstallments();
        StringBuilder output = new StringBuilder();
        
        for (Installment installment : installments) {
            if (!installment.getIsPaid()) {
                output.append("Installment: ")
                        .append(installment.getInstallmentID())
                        .append("  amount: ")
                        .append(installment.getInstallmentAmount())
                        .append("  already paid: ")
                        .append(installment.getPaidAmount())
                        .append("  to pay: ")
                        .append(installment.getInstallmentAmount() - installment.getPaidAmount())
                        .append("  due date: ")
                        .append(DateAdjuster.getDate(installment.getDueDate()));
                break;
            }
        }
        return output.toString();
    }

    @GetMapping("/given")
    public @ResponseBody String getGivenInstallment(int installmentId, HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        Installment installment = installmentRepository.findByInstallmentID(installmentId);

        if (installment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Installment has not been found");
        }

        StringBuilder output = new StringBuilder();
        output.append("Installment: ")
                .append(installment.getInstallmentID())
                    .append("  loan: ")
                    .append(installment.getLoan().getLoanID())
                    .append("  client: ")
                    .append(installment.getLoan().getClient().getClientID())
                    .append("  is paid: ")
                    .append(installment.getIsPaid())
                    .append("  amount: ")
                    .append(installment.getInstallmentAmount())
                    .append("  already paid: ")
                    .append(installment.getPaidAmount())
                    .append("  to pay: ")
                    .append(installment.getInstallmentAmount() - installment.getPaidAmount())
                    .append("  due date: ")
                    .append(DateAdjuster.getDate(installment.getDueDate()));

        return output.toString();
    }

    @GetMapping("/all")
    public @ResponseBody String getAllInstallments(HttpServletRequest request) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        List<Installment> allInstallments = installmentRepository.findAll();
        StringBuilder output = new StringBuilder();

        for (Installment installment: allInstallments) {
            output.append("Installment: ")
                    .append(installment.getInstallmentID())
                    .append("  loan: ")
                    .append(installment.getLoan().getLoanID())
                    .append("  client: ")
                    .append(installment.getLoan().getClient().getClientID())
                    .append("  is paid: ")
                    .append(installment.getIsPaid())
                    .append("  amount: ")
                    .append(installment.getInstallmentAmount())
                    .append("\n");
        }
        return output.toString();
    }
}