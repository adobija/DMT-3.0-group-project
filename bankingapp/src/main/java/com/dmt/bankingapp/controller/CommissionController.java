package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.CommissionRepository;
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
    private final CommissionRepository commissionRepository;

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClient;

    public CommissionController(ClientRepository clientRepository, CommissionRepository commissionRepository, DetailsOfLoggedClientImpl detailsOfLoggedClient) {
        this.clientRepository = clientRepository;
        this.commissionRepository = commissionRepository;
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
        Commission commissionInstance = commissionRepository.findByCommissionOf("LOAN");
        //Check if new commission rate is different
        if(commissionRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(commissionRateAsPercent >= 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Commission oldCommission = commissionInstance;
        //Change name of oldCommission
        String newName = oldCommission.getCommissionOf() + " to " + LocalDateTime.now().toString();
        oldCommission.setCommissionOf(newName);
        //Get old commission rate
        int oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(commissionRateAsPercent);
        //Save to db
        commissionRepository.save(commissionInstance);
        commissionRepository.save(oldCommission);

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
        Commission commissionInstance = commissionRepository.findByCommissionOf("DEPOSIT");
        //Check if new commission rate is different
        if(commissionRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(commissionRateAsPercent >= 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Commission oldCommission = commissionInstance;
        //Change name of oldCommission
        String newName = oldCommission.getCommissionOf() + " to " + LocalDateTime.now().toString();
        oldCommission.setCommissionOf(newName);
        //Get old commission rate
        int oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(commissionRateAsPercent);
        //Save to db
        commissionRepository.save(commissionInstance);
        commissionRepository.save(oldCommission);

        //return
        return "Successfully updated commission rate for DEPOSITS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent();
    }
}
