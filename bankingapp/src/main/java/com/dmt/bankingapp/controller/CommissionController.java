package com.dmt.bankingapp.controller;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.repository.CommissionRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/commission")
public class CommissionController {

    @Autowired
    private final CommissionRepository commissionRepository;

    @Autowired
    private final DetailsOfLoggedClientImpl detailsOfLoggedClient;

    public CommissionController(CommissionRepository commissionRepository, DetailsOfLoggedClientImpl detailsOfLoggedClient) {
        this.commissionRepository = commissionRepository;
        this.detailsOfLoggedClient = detailsOfLoggedClient;
    }

    @PostMapping(path = "/setForLoanCommission")
    public String setCommissionRateForLoanCommission(@RequestParam int commissionRateAsPercent, HttpServletRequest request, Model model){
        //check if accessing client is admin
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        if(!client.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        //Get commission record of loan commission
        Commission commissionInstance = commissionRepository.findByCommissionOf("LOAN_COMMISSION");
        //Check if new commission rate is different
        if(commissionRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(commissionRateAsPercent < 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Commission oldCommission = new Commission(commissionInstance.getCommissionRateInPercent(), commissionInstance.getCommissionOf(), LocalDateTime.now());
        //Change name of oldCommission
        String newName = oldCommission.getCommissionOf() + " rate till " + LocalDateTime.now().toString();
        oldCommission.setCommissionOf(newName);
        //Get old commission rate
        double oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(commissionRateAsPercent);
        //Save to db
        commissionRepository.save(commissionInstance);
        commissionRepository.save(oldCommission);

        model.addAttribute("response", "Successfully updated commission rate for LOANS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent());
        //return
        return "adminTemplates/adminPanel";
    }

    @PostMapping(path = "/setForDeposit")
    public String setCommissionRateForDeposit(@RequestParam int depositRateAsPercent, HttpServletRequest request, Model model){
        //check if accessing client is admin
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        if(!client.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        //Get commission record of deposit
        Commission commissionInstance = commissionRepository.findByCommissionOf("DEPOSIT");
        //Check if new commission rate is different
        if(depositRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New commission rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(depositRateAsPercent < 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Commission oldCommission = new Commission(commissionInstance.getCommissionRateInPercent(), commissionInstance.getCommissionOf(), LocalDateTime.now());
        //Change name of oldCommission
        String newName = oldCommission.getCommissionOf() + " rate till " + LocalDateTime.now().toString();
        oldCommission.setCommissionOf(newName);
        //Get old commission rate
        double oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(depositRateAsPercent);
        //Save to db
        commissionRepository.save(commissionInstance);
        commissionRepository.save(oldCommission);

        model.addAttribute("response", "Successfully updated commission rate for DEPOSITS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent());

        //return
        return "adminTemplates/adminPanel";
    }

    @PostMapping(path = "/setForLoanInterest")
    public String setCommissionRateForLoanInterest(@RequestParam int interestRateAsPercent, HttpServletRequest request, Model model){
        //check if accessing client is admin
        Client client = detailsOfLoggedClient.getLoggedClientInstance(request);
        if(!client.isAdmin()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission!");
        }
        //Get commission record of loan interest
        Commission commissionInstance = commissionRepository.findByCommissionOf("LOAN_INTEREST");
        //Check if new commission rate is different
        if(interestRateAsPercent == commissionInstance.getCommissionRateInPercent()){
            throw new ResponseStatusException(HttpStatus.IM_USED, "New interest rate must be different from previous one!");
        }
        //Check if new commission rate is valid
        if(interestRateAsPercent < 0){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Insert valid new commission rate");
        }
        //Create instance of previous commission rate
        Commission oldCommission = new Commission(commissionInstance.getCommissionRateInPercent(), commissionInstance.getCommissionOf(), LocalDateTime.now());
        //Change name of oldCommission
        String newName = oldCommission.getCommissionOf() + " rate till " + LocalDateTime.now().toString();
        oldCommission.setCommissionOf(newName);
        //Get old commission rate
        double oldRate = commissionInstance.getCommissionRateInPercent();
        oldCommission.setCommissionRateInPercent(oldRate);
        //Set new commission rate
        commissionInstance.setCommissionRateInPercent(interestRateAsPercent);
        //Save to db
        commissionRepository.save(commissionInstance);
        commissionRepository.save(oldCommission);

        model.addAttribute("response", "Successfully updated interest rate for LOANS from " + oldCommission.getCommissionRateInPercent() + " to " + commissionInstance.getCommissionRateInPercent());

        //return
        return "adminTemplates/adminPanel";
    }
}
