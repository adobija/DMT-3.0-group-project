package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private int accountID;

    @Column(name = "accountNumber")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "clientID")
    private Client client;

    @Column(name = "accountBalance")
    private double accountBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "accountType")
    private AccountType accountType;

    public Account(String accountNumber, AccountType accountType, Client client) {
        this.accountNumber = accountNumber;
        this.accountBalance = 0.0;
        this.accountType = accountType;
        this.client = client;
        if (client != null) {
            client.addAccount(this);
        }
    }

    public Account() {}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double newAmountBalance, boolean isExpense) {
        if (isExpense) {
            this.accountBalance -= newAmountBalance;
        } else {
            this.accountBalance += newAmountBalance;
        }
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public enum AccountType {
        CHECKING,
        LOAN,
        DEPOSIT,
        BANK
    }
}
