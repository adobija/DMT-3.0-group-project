package com.dmt.bankingapp.springTests.entitiesTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit.DepositType;

import java.time.LocalDateTime;

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

    @Test
    public void createDepositTest() {

        // Arrange
        double depositAmount = 1500;
        int numberOfMonthsOnDeposit = 36;
        double interestRate = 5;
        Account account = new Account();
        Client client = new Client();
        account.setClient(client);
        DepositType depositType = DepositType.FIXED;

        // Act
        Deposit deposit = new Deposit(interestRate, numberOfMonthsOnDeposit, account, depositAmount, depositType);

        // Assert
        assertNotNull(deposit);
        assertEquals(depositAmount, deposit.getTotalDepositAmount());
        assertEquals(numberOfMonthsOnDeposit, deposit.getDepositDuration());
        assertEquals(interestRate, deposit.getInterestRate());
        assertEquals(depositType, deposit.getDepositType());
        assertNotNull(deposit.getDateOfDeposit());
        assertEquals(client, deposit.getClient());
    }

    @Test
    public void gettersAndSettersTest() {

        // Arrange
        Deposit deposit = new Deposit();
        double depositAmount = 2000;
        int numberOfMonthsOnDeposit = 48;
        double interestRate = 7;
        LocalDateTime now = LocalDateTime.now();
        Account account = new Account();
        Client client = new Client();
        DepositType depositType = DepositType.PROGRESSIVE;

        // Act
        deposit.setTotalDepositAmount(depositAmount);
        deposit.setDepositDuration(numberOfMonthsOnDeposit);
        deposit.setInterestRate(interestRate);
        deposit.setDateOfDeposit(now);
        deposit.setCheckingAccount(account);
        deposit.setClient(client);
        deposit.setDepositType(depositType);

        // Assert
        assertEquals(depositAmount, deposit.getTotalDepositAmount());
        assertEquals(numberOfMonthsOnDeposit, deposit.getDepositDuration());
        assertEquals(interestRate, deposit.getInterestRate());
        assertEquals(now, deposit.getDateOfDeposit());
        assertEquals(account, deposit.getCheckingAccount());
        assertEquals(client, deposit.getClient());
        assertEquals(depositType, deposit.getDepositType());
    }
}