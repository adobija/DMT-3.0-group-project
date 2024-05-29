package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_ID")
    private int transactionID;
    @ManyToOne
    @JoinColumn(name = "Account_of_sender", referencedColumnName = "Account_ID")
    private Account giver;

    @ManyToOne
    @JoinColumn(name = "Account_of_receiver", referencedColumnName = "Account_ID")
    private Account receiver;
    private double amount;

    @Column(name = "Date_of_transaction")
    private LocalDateTime timestamp;

    public Transaction(Account giver, Account receiver, double amount) {
        this.giver = giver;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        manipulateTransaction(giver, receiver, amount);
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
