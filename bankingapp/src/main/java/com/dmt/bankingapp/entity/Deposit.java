package com.dmt.bankingapp.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "Deposits")
public class Deposits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Deposit_ID")
    private int depositID;

    @ManyToOne
    @JoinColumn(name = "Deposit_account", referencedColumnName = "account_ID")
    private Account depositAccount;

    @ManyToOne
    @JoinColumn(name = "Checking_account", referencedColumnName = "account_ID")
    private Account checkingAccount;

    @ManyToOne
    @JoinColumn(name = "Bank_account", referencedColumnName = "account_ID")
    private Account bankAccount;

    @ManyToOne
    @JoinColumn(name = "Client", referencedColumnName = "user_ID")
    private Client client;

    @Column(name = "Principal_deposit_amount")
    private double principalDepositAmount;

    @Column(name = "Deposit_duration")
    private int depositDuration;

    @Column(name = "Interest_rate")
    private double interestRate;

    @Column(name = "Total_deposit_amount")
    private double totalDepositAmount;

    @Column(name = "Date_of_deposit")
    private LocalDateTime timestamp;

    public void Deposit(Account depositAccount, Account checkingAccount, double principalAmount, double interestRate, int depositDuration, Account bankAccount) {
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
    public static void main(String[] args) {

        double fixedTermDeposit = calculateFixedTermDeposit(1000, 12, 10);
        System.out.println("Fixed term: " + fixedTermDeposit);

        double progressiveDeposit = calculateProgressiveDeposit(1000, 4);
        System.out.println("Progressive: " + progressiveDeposit);

    }
}
