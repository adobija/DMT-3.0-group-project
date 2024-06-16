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
    @JoinColumn(name = "checkingAccount", referencedColumnName = "accountID")
    private Account checkingAccount;

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

    @Column(name = "returnOfInvestment")
    private double returnOfInvestment;

    @Enumerated(EnumType.STRING)
    @Column(name = "depositType", nullable = true)
    private DepositType depositType;

    public Deposit(double interestRate, int depositDuration, Account checkingAccount, double totalDepositAmount,
            DepositType depositType) {

        this.checkingAccount = checkingAccount;
        this.interestRate = interestRate;
        this.depositDuration = depositDuration;
        this.totalDepositAmount = totalDepositAmount;
        this.depositType = depositType;

        this.client = checkingAccount.getClient();
        if (client != null) {
            client.addDeposit(this);
        }

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

    public int getDepositDuration() {
        return this.depositDuration;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public DepositType getDepositType() {
        return this.depositType;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getDateOfDeposit() {
        return dateOfDeposit;
    }

    public double getTotalDepositAmount() {
        return this.totalDepositAmount;
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

    public enum DepositType {
        FIXED,
        PROGRESSIVE
    }

    public void setDepositType(DepositType depositType) {
        this.depositType = depositType;
    }

    public void calculateFixedTermDeposit() {
        double depositAmount = getTotalDepositAmount();
        int numberOfMonthsOnDeposit = getDepositDuration();
        double interestRate = getInterestRate();

        double interest = depositAmount * (numberOfMonthsOnDeposit / 12) * (interestRate / 100);
        this.returnOfInvestment = depositAmount + interest;

    }

    public void calculateProgressiveDeposit() {

        double depositAmount = getTotalDepositAmount();
        int numberOfMonthsOnDeposit = getDepositDuration();
        double interestRate = 0.01;
        int numberOfQuarters = (int) numberOfMonthsOnDeposit / 3;

        if (numberOfQuarters > 12) {
            throw new IllegalArgumentException("Number of quarters cannot exceed 12.");
        }

        for (int i = 0; i < numberOfQuarters; i++) {
            double interest = depositAmount * interestRate;
            depositAmount += interest;
            interestRate += 0.01;
        }

        BigDecimal roundToTwoDecimalPlaces = new BigDecimal(Double.toString(depositAmount));
        roundToTwoDecimalPlaces = roundToTwoDecimalPlaces.setScale(2, RoundingMode.HALF_UP);

        this.returnOfInvestment = roundToTwoDecimalPlaces.doubleValue();
    }
}