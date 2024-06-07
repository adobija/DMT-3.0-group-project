package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loanID")
    private int loanID;

    @ManyToOne
    @JoinColumn(name = "loanAccount", referencedColumnName = "accountID")
    private Account loanAccount;

    @ManyToOne
    @JoinColumn(name = "checkingAccount", referencedColumnName = "accountID")
    private Account checkingAccount;

    @ManyToOne
    @JoinColumn(name = "client", referencedColumnName = "clientID")
    private Client client;

    @Column(name = "principalLoanAmount")
    private double principalLoanAmount;

    @Column(name = "loanDuration")
    private int loanDuration;

    @Column(name = "intrestRate")
    private double intrestRate;

    @Column(name = "totalLoanAmount")
    private double totalLoanAmount;

    @Column(name = "dateOfLoan")
    private LocalDateTime timestamp;

    @Column(name = "commisionRate")
    private double commisionRate;

    @ManyToOne
    @JoinColumn(name = "bankAccount", referencedColumnName = "accountID")
    private Account bankAccount;

    @Column(name = "isActive")
    private boolean isActive;


    public Loan(Account loanAccount, Account checkingAccount, double principalAmount, double intrestRate,
            double commisionRate, int loanDuration, Account bankAccount) {
        this.loanAccount = loanAccount;
        this.checkingAccount = checkingAccount;
        this.bankAccount = bankAccount;
        this.principalLoanAmount = principalAmount;
        this.intrestRate = intrestRate;
        this.commisionRate = commisionRate;
        this.loanDuration = loanDuration;

        this.client = checkingAccount.getClient();
        if (client != null) {
            client.addLoan(this);
        }

        grantLoan(this.loanAccount, this.checkingAccount, this.principalLoanAmount, this.intrestRate, this.commisionRate, this.loanDuration, this.bankAccount);

        if (this.totalLoanAmount > 0) {
            this.isActive = true;
        }
    }

    public Loan() {
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

    public Account getBankAccount() {
        return this.bankAccount;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
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

    public void setCommisionRate(double commsionInPercent) {
        this.commisionRate = commsionInPercent;
    }

    public double getCommisionRate() {
        return this.commisionRate;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    // Method to calculate the amount of interests that bank charges for launching
    // the loan, basing on amout of money to be borrowed, intrest rate and duration
    // of the loan
    public double intrestAmount(double principalAmount, double intrestRate, int loanDuration) {
        double convertedRate = intrestRate * 0.01; // converting from % value to decimal value (i.e. 3% to 0.03)
        double total = principalAmount * (1 + ((convertedRate * loanDuration) / 12));
        double intrestAmount = total - principalAmount;
        return intrestAmount;
    }

    // Method to calculate commision charged by the bank when granting the loan
    public double commisionAmout(double principalAmount, double commisionRate) {
        double decimalRate = intrestRate * 0.01; // converting from % value to decimal value (i.e. 3% to 0.03)
        double commision = principalAmount * decimalRate;
        return commision;
    }

    public void grantLoan(Account loanAccount, Account checkingAccount, double principalAmount, double intrestRate,
            double commisionRate, int loanDuration, Account bankAccount) {
        // Calculating commision and amount of intrests
        double intrestForBank = intrestAmount(principalAmount, intrestRate, loanDuration);
        double commisionForBank = commisionAmout(principalAmount, commisionRate);
        // Money transfer from the account where loan is launched to the checking
        // account of the customer + setting time and date for the loan
        Transaction principalGranted = new Transaction(loanAccount, checkingAccount, principalAmount);
        setTimestamp(LocalDateTime.now());
        // Profit from intrest transfer from the account where loan is launched to the
        // bank's account
        Transaction bankProfit = new Transaction(loanAccount, bankAccount, intrestForBank);
        // Profit from commision transfer from the account where loan is launched to the
        // bank's account
        Transaction commisionProfit = new Transaction(loanAccount, bankAccount, commisionForBank);
        // Updating total amount of the loan
        double totalLoanAmount = principalAmount + intrestForBank + commisionForBank;
        setTotalLoanAmout(totalLoanAmount);
    }
}
