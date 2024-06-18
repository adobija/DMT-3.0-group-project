package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Comission;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.ComissionRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/commission")
public class CommissionController {

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final ComissionRepository comissionRepository;

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClient;

    public CommissionController(ClientRepository clientRepository, ComissionRepository comissionRepository, DetailsOfLoggedClientImpl detailsOfLoggedClient) {
        this.clientRepository = clientRepository;
        this.comissionRepository = comissionRepository;
        this.detailsOfLoggedClient = detailsOfLoggedClient;
    }

    @PostMapping(path = "/setForLoan")
    public String setCommissionRateForLoan(@RequestParam int commissionRateAsPercent, HttpServletRequest request){
        //check if accessing client is admin
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        if(!client.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        //Get commission record of loan
        Comission commissionInstance = comissionRepository.findByComissionOf("LOAN");
        //Check if new commission rate is different
        if(commissionRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(commissionRateAsPercent >= 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Comission oldCommission = commissionInstance;
        //Change name of oldCommission
        String newName = oldCommission.getComissionOf() + " to " + LocalDateTime.now().toString();
        oldCommission.setComissionOf(newName);
        //Get old commission rate
        int oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(commissionRateAsPercent);
        //Save to db
        comissionRepository.save(commissionInstance);
        comissionRepository.save(oldCommission);

        //return
        return "Successfully updated commission rate for LOANS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent();
    }

    @PostMapping(path = "/setForDeposit")
    public String setCommissionRateForDeposit(@RequestParam int commissionRateAsPercent, HttpServletRequest request){
        //check if accessing client is admin
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        if(!client.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        //Get commission record of deposit
        Comission commissionInstance = comissionRepository.findByComissionOf("DEPOSIT");
        //Check if new commission rate is different
        if(commissionRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(commissionRateAsPercent >= 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Comission oldCommission = commissionInstance;
        //Change name of oldCommission
        String newName = oldCommission.getComissionOf() + " to " + LocalDateTime.now().toString();
        oldCommission.setComissionOf(newName);
        //Get old commission rate
        int oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(commissionRateAsPercent);
        //Save to db
        comissionRepository.save(commissionInstance);
        comissionRepository.save(oldCommission);

        //return
        return "Successfully updated commission rate for DEPOSITS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent();
    }
}
