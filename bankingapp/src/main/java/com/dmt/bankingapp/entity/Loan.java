package com.dmt.bankingapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_ID")
    private int loanID;

    @ManyToOne
    @JoinColumn(name = "Checking_account", referencedColumnName = "Account_ID")
    private Account checkingAccount;

    @ManyToOne
    @JoinColumn(name = "Loan_account", referencedColumnName = "Account_ID")
    private Account loanAccount;

    @ManyToOne
    @JoinColumn(name = "User", referencedColumnName = "User_ID")
    private User user;

    private double loanAmount;

    @Column(name = "Date_of_loan")
    private LocalDateTime timestamp;

    public Loan(Account checkingAccount, Account loanAccount, double loanAmount) {
        this.loanAccount = loanAccount;
        this.checkingAccount = checkingAccount;
        this.user = checkingAccount.getUser();
        if (user != null) {
            user.addLoan(this);
        }
        this.loanAmount = loanAmount;
    }

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public Account getLoanAccount() {
        return this.loanAccount;
    }

    public Account getCheckingAccount() {
        return this.checkingAccount;
    }

    public double getLoanAmount() {
        return this.loanAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void grantLoan(Account checkingAccount, Account loanAccount, double loanAmount) {
        Transaction loan = new Transaction(loanAccount, checkingAccount, loanAmount);
        loan.manipulateTransaction(loanAccount, checkingAccount, loanAmount);
        // Here will be invoked method to increase amount of money to be returned
        // when intrest rates are applied. This will be displayed on loan account.

    }
}
