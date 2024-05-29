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
    private Account checkingAccountID;

    @ManyToOne
    @JoinColumn(name = "Loan_account", referencedColumnName = "Account_ID")
    private Account loanAccountID;

    @ManyToOne
    @JoinColumn(name = "User", referencedColumnName = "User_ID")
    private User user;

    private double loanAmount;

    @Column(name = "Date_of_loan")
    private LocalDateTime timestamp;

    public Loan(Account loanAccountID, double loanAccountBalance, Account checkingAccountID, User user) {
        this.loanAccountID = loanAccountID;
        this.loanAmount = loanAccountBalance;
        this.checkingAccountID = checkingAccountID;
        this.user = user;
        if (user != null) {
            user.addLoan(this);
        }
    }

    public int getLoanID() {
        return loanID;
    }

    public void setLoanID(int loanID) {
        this.loanID = loanID;
    }

    public Account getLoanAccountID() {
        return this.loanAccountID;
    }

    public Account getCheckingAccountID() {
        return this.checkingAccountID;
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
}
