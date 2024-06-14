package com.dmt.bankingapp.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depositID")
    private Integer depositID;

    @ManyToOne
    @JoinColumn(name = "depositAccount", referencedColumnName = "accountID")
    private Account depositAccount;

    @ManyToOne
    @JoinColumn(name = "checkingAccount", referencedColumnName = "accountID")
    private Account checkingAccount;

    @ManyToOne
    @JoinColumn(name = "bankAccount", referencedColumnName = "accountID")
    private Account bankAccount;

    @ManyToOne
    @JoinColumn(name = "client", referencedColumnName = "clientID")
    private Client client;

    @Column(name = "interestRate")
    private double interestRate;

    @Column(name = "depositDuration")
    private int depositDuration;

    @Column(name = "totalDepositAmount")
    private double totalDepositAmount;

    @Column(name = "dateOfDeposit")
    private LocalDateTime dateOfDeposit;

    public Deposit( double interestRate, int depositDuration, Account checkingAccount, double totalDepositAmount, String depositType) {
        
        this.checkingAccount = checkingAccount;
        this.interestRate = interestRate;
        this.depositDuration = depositDuration;
        this.totalDepositAmount = totalDepositAmount;
    
         this.client = checkingAccount.getClient();
         if (client != null) {
             client.addDeposit(this);
         }

        this.totalDepositAmount = 0;
        this.dateOfDeposit = LocalDateTime.now();
        
        }
        
    public Deposit() {

    }

    public Integer getDepositID() {
        return depositID;
    }

    public Account getCheckingAccount() {
        return this.checkingAccount;
    }

    public Account getBankAccount() {
        return this.bankAccount;
    }

    public double getDepositDuration() {
        return this.depositDuration;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getDateOfDeposit() {
        return dateOfDeposit;
    }

    public double getTotalDepositAmount() {
        return totalDepositAmount;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDepositDuration(int depositDuration) {
        this.depositDuration = depositDuration;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public void setTotalDepositAmount(double totalDepositAmount) {
        this.totalDepositAmount = totalDepositAmount;
    }

    public void setDateOfDeposit(LocalDateTime timestamp) {
        this.dateOfDeposit = timestamp;
    }

    public static double calculateFixedTermDeposit(double depositAmount, int numberOfMonthsOnDeposit,
            int interestRate) {
        double interest = depositAmount * (numberOfMonthsOnDeposit / 12) * (interestRate / 100);
        return depositAmount + interest;
    }

    public static double calculateProgressiveDeposit(double depositAmount, int numberOfQuarters) {
        if (numberOfQuarters > 4) {
            throw new IllegalArgumentException("Number of quarters cannot exceed 4.");
        }

        double[] interestRates = { 0.02, 0.04, 0.06, 0.08 }; // Quarterly interest rates

        for (int i = 0; i < numberOfQuarters; i++) {
            double interest = depositAmount * interestRates[i];
            depositAmount += interest;
        }

        BigDecimal roundToTwoDecimalPlaces = new BigDecimal(Double.toString(depositAmount));
        roundToTwoDecimalPlaces = roundToTwoDecimalPlaces.setScale(2, RoundingMode.HALF_UP);
        return roundToTwoDecimalPlaces.doubleValue();
    }

}
