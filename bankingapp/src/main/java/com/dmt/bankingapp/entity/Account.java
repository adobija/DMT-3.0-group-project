package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_ID")
    private int accountID;

    @Column(name = "account_number")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;

    @Column(name = "account_balance")
    private double accountBalance;

    @Column(name = "account_type")
    private String accountType;

    public Account(String accountNumber, String accountType, User user) {
        this.accountNumber = accountNumber;
        this.accountBalance = 0.0;
        this.accountType = accountType;
        this.user = user;
        if (user != null) {
            user.addAccount(this);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
