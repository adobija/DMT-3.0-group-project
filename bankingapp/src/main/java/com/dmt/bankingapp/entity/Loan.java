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
        this.bankAccount = bankAccount;
        this.principalLoanAmount = principalAmount;
        this.intrestRate = intrestRate;
        this.loanDuration = loanDuration;

        this.user = checkingAccount.getUser();
        if (user != null) {
            user.addLoan(this);
        }

        this.totalLoanAmount = 0;
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

    public Account getBankAccount() {
        return this.bankAccount;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPrincipalLoanAmout(double principalAmount) {
        this.principalLoanAmount = principalAmount;
    }

    public double getPrincipalLoanAmount() {
        return this.principalLoanAmount;
    }

    public void setTotalLoanAmout(double totalAmount) {
        this.totalLoanAmount = totalAmount;
    }

    public double getTotalLoanAmount() {
        return this.totalLoanAmount;
    }

    public void setLoanDuration(int durationInMonths) {
        this.loanDuration = durationInMonths;
    }

    public int getLoanDuration() {
        return this.loanDuration;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setIntrestRate(double rateInPercent) {
        this.intrestRate = rateInPercent;
    }

    public double getIntrestRate() {
        return this.intrestRate;
    }

    // Method to calculate amount of interests that bank charges for launching the loan, basing on amout of money to be borrowed, intrest rate and duration of the loan
    public double intrestAmount(double principalAmount, double intrestRate, int loanDuration) {
        double convertedRate = intrestRate * 0.01; //converting from % value to decimal value (i.e. 3% to 0.03)
        double total = principalAmount * (1 + ((convertedRate * loanDuration) / 12)); 
        double intrestAmount = total - principalAmount;
        return intrestAmount;
    }

    public void grantLoan(Account loanAccount, Account checkingAccount, double principalAmount, double intrestRate, int loanDuration, Account bankAccount) {
        double intrestForBank = intrestAmount(principalAmount, intrestRate, loanDuration);
        // Money transfer from the account where loan is launched to the checking account of the customer + setting time and date for the loan
        Transaction principalGranted = new Transaction(loanAccount, checkingAccount, principalAmount);
        setTimestamp(LocalDateTime.now());
        // Profit from intrest transfer from the account where loan is launched to the bank's account
        Transaction bankProfit = new Transaction(loanAccount, bankAccount, intrestForBank);
        // Updating total amount of the loan
        double totalLoanAmount = principalAmount + intrestForBank;
        setTotalLoanAmout(totalLoanAmount);
    } 
}
