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

    @Column(name = "Principal_loan_amount")
    private double principalLoanAmount;

    @Column(name = "Loan_duration")
    private int loanDuration;

    @Column(name = "Intrest_rate")
    private double intrestRate;

    @Column(name = "Total_loan_amount")
    private double totalLoanAmount;

    @Column(name = "Date_of_loan")
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "Bank_account", referencedColumnName = "account_ID")
    private Account bankAccount;

    public Loan(Account loanAccount, Account checkingAccount, double principalAmount, double intrestRate, int loanDuration, Account bankAccount) {
        this.loanAccount = loanAccount;
        this.checkingAccount = checkingAccount;
        this.user = checkingAccount.getUser();
        if (user != null) {
            user.addLoan(this);
        }
        this.principalLoanAmount = principalAmount;
        this.intrestRate = intrestRate;
        this.loanDuration = loanDuration;
        this.bankAccount = bankAccount;
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

    public double getPrincipalLoanAmount() {
        return this.principalLoanAmount;
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

    // Method to calculate amount of interests that bank charges for launching the loan, basing on amout of money to be borrowed, intrest rate and duration of the loan
    public double intrestAmount(double principalAmount, double intrestRate, int loanDuration) {
        double total = principalAmount * (1 + ((intrestRate * loanDuration) / 12)); 
        double intrestAmount = total - principalAmount;
        return intrestAmount;
    }

    public void grantLoan(Account loanAccount, Account checkingAccount, double principalAmount, double intrestRate, int loanDuration, Account bankAccount) {

        Transaction loan = new Transaction(loanAccount, checkingAccount, principalAmount);
        loan.setTimestamp(LocalDateTime.now());
    }

    
}
