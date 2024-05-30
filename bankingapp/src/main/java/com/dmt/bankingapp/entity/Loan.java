package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Loan_ID")
    private int loanID;

    @ManyToOne
    @JoinColumn(name = "Loan_account", referencedColumnName = "account_ID")
    private Account loanAccount;

    @ManyToOne
    @JoinColumn(name = "Checking_account", referencedColumnName = "account_ID")
    private Account checkingAccount;

    @ManyToOne
    @JoinColumn(name = "Client", referencedColumnName = "user_ID")
    private User user;

    @Column(name = "Principle_loan_amount")
    private double principleLoanAmount;

    @Column(name = "Loan_duration")
    private int loanDuration;

    @Column(name = "Intrest_rate")
    private double intrestRate;

    @Column(name = "Total_loan_amount")
    private double totalLoanAmount;

    @Column(name = "Date_of_loan")
    private LocalDateTime timestamp;

    public Loan(Account loanAccount, Account checkingAccount, double principleAmount, double intrestRate, int loanDuration) {
        this.loanAccount = loanAccount;
        this.checkingAccount = checkingAccount;
        this.user = checkingAccount.getUser();
        if (user != null) {
            user.addLoan(this);
        }
        this.principleLoanAmount = principleAmount;
        this.intrestRate = intrestRate;
        this.loanDuration = loanDuration;
    }

    public Loan() {}

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

    public double getPrincipleLoanAmount() {
        return this.principleLoanAmount;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setIntrestRate(double intrestRate) {
        this.intrestRate = intrestRate;
    }

    public double getIntrestRate() {
        return this.intrestRate;
    }

    public void grantLoan(Account loanAccount, Account checkingAccount, double principleAmount, double intrestRate, int loanDuration) {

        Transaction loan = new Transaction(loanAccount, checkingAccount, principleAmount);
        loan.setTimestamp(LocalDateTime.now());
        // Here will be invoked method to increase amount of money to be returned
        // when interest rates are applied. This will be displayed on loan account.
    }

    public double intrestAmount(double principleAmount, double intrestRate, int loanDuration) {
        double total = 0.0;
        return total;
    }
}
