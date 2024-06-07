package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.dmt.bankingapp.entity.Account.AccountType;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    private int transactionID;
    
    @ManyToOne
    @JoinColumn(name = "accountOfSender", referencedColumnName = "accountID")
    private Account giver;

    @ManyToOne
    @JoinColumn(name = "accountOfReceiver", referencedColumnName = "accountID")
    private Account receiver;
    private double amount;

    @Column(name = "dateOfTransaction")
    private LocalDateTime timestamp;

    public Transaction(Account giver, Account receiver, double amount) {
        this.giver = giver;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        manipulateTransaction(giver, receiver, amount);

        // Method to update paid installments
        if (receiver.getAccountType().equals(AccountType.LOAN)) {

        }
    }

    public Transaction() {
    }

    public Account getGiver() {
        return giver;
    }

    public void setGiver(Account giver) {
        this.giver = giver;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void manipulateTransaction(Account giver, Account receiver, double amount){
        giver.setAccountBalance(amount, true);
        receiver.setAccountBalance(amount, false);
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
