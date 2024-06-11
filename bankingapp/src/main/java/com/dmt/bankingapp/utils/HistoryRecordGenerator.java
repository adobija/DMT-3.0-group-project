package com.dmt.bankingapp.utils;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.record.History;

import java.time.LocalDateTime;

public class HistoryRecordGenerator {
    public static History castDataToCreateHistoryRecord(Transaction transaction, Account accountOfClientAccessing){
        boolean receivingTransaction;
        if(transaction.getGiver().getAccountNumber().equalsIgnoreCase(accountOfClientAccessing.getAccountNumber())){
            receivingTransaction = false;
        }else{
            receivingTransaction = true;
        }
        String contributorName, contributorAccountNumber, amount, typeOf;
        LocalDateTime date;
        Integer id;
        if(receivingTransaction){
            typeOf = "Reciving from: ";
            contributorName = transaction.getGiver().getClient().getClientName();
            contributorAccountNumber = transaction.getGiver().getAccountNumber();
            amount = "+"+Double.toString(transaction.getAmount());
            date = transaction.getTimestamp();
            id = transaction.getTransactionID();
        }else{
            typeOf = "Sending to: ";
            contributorName = transaction.getReceiver().getClient().getClientName();
            contributorAccountNumber = transaction.getReceiver().getAccountNumber();
            amount = "-"+Double.toString(transaction.getAmount());
            date = transaction.getTimestamp();
            id = transaction.getTransactionID();
        }
        return new History(typeOf, contributorName, contributorAccountNumber, amount+" zł", date, id);
    }
}
