package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_ID")
    private int loanID;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;

    private int loanAccountID;
    private double loanAccountBalance;
    private int checkingAccountID;

    public Loan(int loan_account_ID, double loanAccountBalance, int checkingAccountID, User user) {
        this.loanAccountID = loan_account_ID;
        this.loanAccountBalance = loanAccountBalance;
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

    public int getLoanAccountID() {
        return this.loanAccountID;
    }

    public int getCheckingAccountID() {
        return this.checkingAccountID;
    }

    public double getLoanAccountBalance() {
        return this.loanAccountBalance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
