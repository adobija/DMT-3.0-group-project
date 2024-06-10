package com.dmt.bankingapp.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depositID")
    private int depositID;

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

    @Column(name = "principalDepositAmount")
    private double principalDepositAmount;

    @Column(name = "depositDuration")
    private int depositDuration;

    @Column(name = "intrestRate")
    private double interestRate;

    @Column(name = "totalDepositAmount")
    private double totalDepositAmount;

    @Column(name = "dateOfDeposit")
    private LocalDateTime timestamp;

    public Deposit(Account depositAccount, Account checkingAccount, double principalAmount, double interestRate, int depositDuration, Account bankAccount
         ) {
        this.depositAccount = depositAccount;
        this.checkingAccount = checkingAccount;
        this.bankAccount = bankAccount;
        this.principalDepositAmount = principalAmount;
        this.interestRate = interestRate;
        this.depositDuration = depositDuration;

         this.client = checkingAccount.getClient();
         if (client != null) {
             client.addDeposit(this);
         }

        this.totalDepositAmount = 0;
    }

    public Deposit() {
        
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static double calculateFixedTermDeposit(double depositAmount, int numberOfMonthsOnDeposit, int interestRate) {
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
