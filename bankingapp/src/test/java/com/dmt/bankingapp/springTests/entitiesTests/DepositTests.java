package com.dmt.bankingapp.springTests.entitiesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.dmt.bankingapp.entity.Deposit;

public class DepositTests {

    @Test
    public void calculateFixedTermDepositTest() {

        // Arrange
        double depositAmount = 1000;
        int numberOfMonthsOnDeposit = 24;
        double interestRate = 10;

        Deposit deposit = new Deposit();
        deposit.setTotalDepositAmount(depositAmount);
        deposit.setDepositDuration(numberOfMonthsOnDeposit);
        deposit.setInterestRate(interestRate);

        // Act
        deposit.calculateFixedTermDeposit();
        double returnOfInvestment = deposit.getReturnOfInvestment();

        // Assert
        assertEquals(1200, returnOfInvestment);
    }

    @Test
    public void calculateProgressiveDepositTest() {

        // Arrange
        double depositAmount = 1000;
        int numberOfMonthsOnDeposit = 12;


        Deposit depositProgressive = new Deposit();
        depositProgressive.setTotalDepositAmount(depositAmount);
        depositProgressive.setDepositDuration(numberOfMonthsOnDeposit);

        // Act
        depositProgressive.calculateProgressiveDeposit();
        double returnOfInvestment = depositProgressive.getReturnOfInvestment();

        // Assert
        assertEquals(1103.55, returnOfInvestment);
    }

    @Test
    public void calculateProgressiveDepositTest2() {

        // Arrange
        double depositAmount = 1000;
        int numberOfMonthsOnDeposit = 24;

        Deposit depositProgressive = new Deposit();
        depositProgressive.setTotalDepositAmount(depositAmount);
        depositProgressive.setDepositDuration(numberOfMonthsOnDeposit);

        // Act
        depositProgressive.calculateProgressiveDeposit();
        double returnOfInvestment = depositProgressive.getReturnOfInvestment();

        // Assert
        assertEquals(1419.37, returnOfInvestment);
    }
}