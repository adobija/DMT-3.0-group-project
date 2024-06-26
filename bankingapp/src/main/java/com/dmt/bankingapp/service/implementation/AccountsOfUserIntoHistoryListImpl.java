package com.dmt.bankingapp.service.implementation;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.record.History;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.interfaceClass.AccountsOfUserIntoHistoryList;
import com.dmt.bankingapp.utils.HistoryRecordGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AccountsOfUserIntoHistoryListImpl implements AccountsOfUserIntoHistoryList {

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final TransactionRepository transactionRepository;

    public AccountsOfUserIntoHistoryListImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ResponseEntity<ArrayList<History>> getStoredHistoryByClient(Client client){
        //find instance of client's account
        List<Account> accountList = accountRepository.findByClient(client);
        if(accountList.isEmpty()){
            HttpHeaders header = new HttpHeaders();
            header.add("ErrorMessage", "Client don't have any active accounts!");
            return new ResponseEntity<>(null, header, HttpStatus.NOT_FOUND);
        }
        //Array list of history instance records
        ArrayList<History> historyArrayList = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            Account currentlyWorkingOn = accountList.get(i);
            System.out.println(currentlyWorkingOn.getAccountNumber());
            System.out.println(".".repeat(100));
            //fetch all records in Transaction table that have ID of account
            //All transaction where found account was receiver
            List<Transaction> incomeTransactions = transactionRepository.findByReceiver(currentlyWorkingOn);
            //All transaction where found account was giver
            List<Transaction> expenseTransactions = transactionRepository.findByGiver(currentlyWorkingOn);


            for(Transaction x : incomeTransactions){
                //for each loop to convert and add data from list of transactions to readable history records
                History historyRecord = HistoryRecordGenerator.castDataToCreateHistoryRecord(x,currentlyWorkingOn);
                historyArrayList.add(historyRecord);
            }
            for(Transaction x : expenseTransactions){
                //for each loop to convert and add data from list of transactions to readable history records
                History historyRecord = HistoryRecordGenerator.castDataToCreateHistoryRecord(x,currentlyWorkingOn);
                historyArrayList.add(historyRecord);
            }

            i++;
            accountList.remove(currentlyWorkingOn);
        }

        //Sort history to chronological order by date of transaction
        historyArrayList.sort(Comparator.comparing(History::dateOfTransaction));
        //return sorted history

        ResponseEntity<ArrayList<History>> output = ResponseEntity.ok(historyArrayList);
        return output;
    }
}
