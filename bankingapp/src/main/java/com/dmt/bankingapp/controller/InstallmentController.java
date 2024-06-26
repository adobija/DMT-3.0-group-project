package com.dmt.bankingapp.controller;

import java.util.ArrayList;
import java.util.List;

import com.dmt.bankingapp.record.installments.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

@Controller
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

    @GetMapping("/myAll")
    public String getMyInstallments(HttpServletRequest request, Model model) {
        String clientName = detailsOfLoggedClient.getNameFromClient(request);
        Client client = clientRepository.findByClientName(clientName);
        List<Loan> loans = client.getLoansList();
        ArrayList<MyInstallment> installmentsList = new ArrayList<>();
        for (Loan loan : loans) {
            List<Installment> installments = loan.getInstallments();
            for (Installment installment: installments) {
                installmentsList.add(new MyInstallment(loan.getLoanAccount().getAccountNumber(), loan.getLoanID(), installment.getInstallmentID(), installment.getInstallmentAmount(), installment.getPaidAmount(), installment.getIsPaid(), DateAdjuster.getDate(installment.getDueDate())));
            }
        }
        model.addAttribute("myAll", installmentsList);
        return "installmentTemplates/myAll";
    }

    @GetMapping("/next")
    public String getNextInstallment(int loanId, HttpServletRequest request, Model model) {
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

        ArrayList<NextInstallment> installmentsList = new ArrayList<>();
        for (Installment installment : installments) {
            if (!installment.getIsPaid()) {
                installmentsList.add(new NextInstallment(installment.getLoan().getLoanAccount().getAccountNumber(), installment.getInstallmentID(), installment.getInstallmentAmount(), installment.getPaidAmount(), (installment.getInstallmentAmount() - installment.getPaidAmount()), DateAdjuster.getDate(installment.getDueDate())));
                break;
            }
        }
        model.addAttribute("next", installmentsList);
        return "installmentTemplates/next";
    }

    @GetMapping("/given")
    public String getGivenInstallment(int installmentId, HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        Installment installment = installmentRepository.findByInstallmentID(installmentId);

        if (installment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Installment has not been found");
        }
        GivenInstallment givenInstallment = new GivenInstallment(installment.getLoan().getLoanAccount().getAccountNumber(), installment.getInstallmentID(), installment.getLoan().getLoanID(), installment.getLoan().getClient().getClientID(), installment.getIsPaid(), installment.getInstallmentAmount(), installment.getPaidAmount(), (installment.getInstallmentAmount() - installment.getPaidAmount()), DateAdjuster.getDate(installment.getDueDate()));
        model.addAttribute("given", givenInstallment);
        return "installmentTemplates/given";
    }

    @GetMapping("/loan")
    public String getLoanInstallments(int loanId, HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        Loan loan = loanRepository.findByLoanID(loanId);
        if (loan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan has not been found");
        }

        List<Installment> installments = loan.getInstallments();
        ArrayList<LoanInstallment> loanInstallments = new ArrayList<>();

        for (Installment installment : installments) {
               loanInstallments.add(new LoanInstallment( installment.getLoan().getLoanAccount().getAccountNumber(), installment.getInstallmentID(), installment.getInstallmentAmount(), installment.getIsPaid(), installment.getPaidAmount(), (installment.getInstallmentAmount() - installment.getPaidAmount()), DateAdjuster.getDate(installment.getDueDate())));
        }
        model.addAttribute("forLoan", "installments for loan ID: " + loanId);
        model.addAttribute("loan", loanInstallments);
        return "installmentTemplates/loan";
    }

    @GetMapping("/all")
    public String getAllInstallments(HttpServletRequest request, Model model) {
        String requesterName = detailsOfLoggedClient.getNameFromClient(request);
        Client requester = clientRepository.findByClientName(requesterName);
        if (!requester.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }

        List<Installment> allInstallments = installmentRepository.findAll();

        ArrayList<AllInstallment> allInstallmentsList = new ArrayList<>();
        for (Installment installment: allInstallments) {
           allInstallmentsList.add(new AllInstallment(installment.getLoan().getLoanAccount().getAccountNumber(), installment.getInstallmentID(), installment.getLoan().getLoanID(), installment.getLoan().getClient().getClientID(), installment.getIsPaid(), installment.getInstallmentAmount()));
        }
        model.addAttribute("all", allInstallmentsList);
        return "installmentTemplates/all";
    }
}