package com.dmt.bankingapp.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.dmt.bankingapp.utils.DateAdjuster;
import jakarta.persistence.*;

import com.dmt.bankingapp.utils.DecimalPlacesAdjuster;

@Entity
@Table(name = "Deposits")
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "depositId")
    private Integer depositID;

    @ManyToOne
    @JoinColumn(name = "checkingAccount", referencedColumnName = "accountId")
    private Account checkingAccount;

    @Column(name = "isActive")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "client", referencedColumnName = "clientId")
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

    @Column(name = "dateOfWithdrawn")
    private LocalDateTime dateOfWithdrawn;

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

        if (this.totalDepositAmount > 0) {
            this.isActive = true;
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
    public String getFormatedDate(){
        return DateAdjuster.getDate(dateOfDeposit);
    }

    public double getTotalDepositAmount() {
        return this.totalDepositAmount;
    }

    public double getReturnOfInvestment() {
        return returnOfInvestment;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setCheckingAccount(Account checkingAccount) {
        this.checkingAccount = checkingAccount;
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

    public void setReturnOfInvestment(double returnOfInvestment) {
        this.returnOfInvestment = returnOfInvestment;
    }

    public void calculateFixedTermDeposit() {
        double depositAmount = getTotalDepositAmount();
        int numberOfMonthsOnDeposit = getDepositDuration();
        double interestRate = getInterestRate();

        double interest = depositAmount * (numberOfMonthsOnDeposit / 12.0) * (interestRate / 100.0);
        this.returnOfInvestment = DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(depositAmount + interest);
    }

    public void calculateProgressiveDeposit() {
        double depositAmount = getTotalDepositAmount();
        int numberOfMonthsOnDeposit = getDepositDuration();
        double interestRate = 0.01;
        int numberOfQuarters = numberOfMonthsOnDeposit / 3;

        if (numberOfQuarters > 12) {
            throw new IllegalArgumentException("Number of quarters cannot exceed 12.");
        }

        for (int i = 0; i < numberOfQuarters; i++) {
            double interest = depositAmount * interestRate;
            depositAmount += interest;
            interestRate += 0.01;
        }

        this.returnOfInvestment = DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(depositAmount);
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDateOfWithdrawn(LocalDateTime dateOfWithdrawn) {
        this.dateOfWithdrawn = dateOfWithdrawn;
    }

    public LocalDateTime getDateOfWithdrawn() {
        return dateOfWithdrawn;
    }

    public String getFormatedDateOfWithdrawn(){
        if(dateOfWithdrawn == null){
            return "Hasn't been withdrawn yet!";
        }else{
            return DateAdjuster.getDate(dateOfWithdrawn);
        }
    }

    public String getDepositTypeString(){
        String output = this.getDepositType().toString();
        return output;
    }
}