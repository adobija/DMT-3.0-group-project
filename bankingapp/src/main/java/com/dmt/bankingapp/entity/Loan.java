package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_ID")
    private int loanID;
    private String account_number;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;
    private double account_balance;
    private String account_type;

    public Account(String account_number, double account_balance, String account_type, User user) {
        this.account_number = account_number;
        this.account_balance = account_balance;
        this.account_type = account_type;
        this.user = user;
        if (user != null) {
            user.addAccount(this);
        }
    }

    public Account() {
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return account_number;
    }

    public void setAccountNumber(String account_number) {
        this.account_number = account_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAccountBalance() {
        return account_balance;
    }

    public void setAccountBalance(double account_balance) {
        this.account_balance = account_balance;
    }

    public String getAccountType() {
        return account_type;
    }

    public void setAccountType(String account_type) {
        this.account_type = account_type;
    }
}
