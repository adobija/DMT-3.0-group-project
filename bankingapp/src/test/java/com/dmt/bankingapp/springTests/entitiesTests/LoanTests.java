package com.dmt.bankingapp.springTests.entitiesTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

@DataJpaTest
@Transactional
public class LoanTests {

    @Test
    public void testLoanConstructor() {
        // Arrange
        Client client = new Client("loanTaker", false, "abc123");
        Client bankClient = new Client("bankName", false, "1234");
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, client);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, bankClient);
        double principalAmount = 50000.0;
        double interestRate = 2.9;
        double commisionRate = 4.0;
        int loanDuration = 12;

        // Act
        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);

        // Assert
        assertEquals(loanAccount, loan.getLoanAccount());
        assertEquals(checkingAccount, loan.getCheckingAccount());
        assertEquals(bankAccount, loan.getBankAccount());
        assertEquals(principalAmount, loan.getPrincipalLoanAmount());
        assertEquals(interestRate, loan.getInterestRate());
        assertEquals(commisionRate, loan.getCommisionRate());
        assertEquals(loanDuration, loan.getLoanDuration());
        assertNotNull(loan.getClient());
        assertEquals(loan.getClient(), checkingAccount.getClient());
    }

    @Test
    public void testLoanGettersAndSetters() {
        // Arrange
        Loan loan = new Loan();
        int loanID = 1;
        double principalAmount = 300000.0;
        double totalAmount = 12000.0;
        int loanDuration = 360;
        double interestRate = 6.5;
        double commisionRate = 4.0;
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        loan.setLoanID(loanID);
        loan.setPrincipalLoanAmout(principalAmount);
        loan.setTotalLoanAmout(totalAmount);
        loan.setLoanDuration(loanDuration);
        loan.setInterestRate(interestRate);
        loan.setCommisionRate(commisionRate);
        loan.setDateOfLoan(timestamp);

        // Assert
        assertEquals(loanID, loan.getLoanID());
        assertEquals(principalAmount, loan.getPrincipalLoanAmount());
        assertEquals(totalAmount, loan.getTotalLoanAmount());
        assertEquals(loanDuration, loan.getLoanDuration());
        assertEquals(interestRate, loan.getInterestRate());
        assertEquals(commisionRate, loan.getCommisionRate());
        assertEquals(timestamp, loan.getDateOfLoan());
    }

    @Test
    public void testInactiveLoan() {
        // Arrange
        Loan loan = new Loan();
        
        // Act
        loan.setIsActive(false);

        // Assert
        assertEquals(false, loan.getIsActive());
    }

    @Test
    public void testActiveLoan() {
        // Arrange
        Loan loan = new Loan();

        // Act
        loan.setIsActive(true);

        // Assert
        assertEquals(true, loan.getIsActive());
    }

    @Test
    public void testDateOfLoan() {
        // Arrange
        Loan loan = new Loan();
        LocalDateTime now = LocalDateTime.now();

        // Act
        loan.setDateOfLoan(now);

        // Assert
        assertEquals(now, loan.getDateOfLoan());
    }

    @Test
    public void testLeftToPay() {
        // Arrange
        Loan loan = new Loan();
        double leftToPay = 15000.0;

        // Act
        loan.setLeftToPay(leftToPay);

        // Assert
        assertEquals(leftToPay, loan.getLeftToPay());
    }

    @Test
    public void testSetAndGetClient() {
        // Arrange
        Client client = new Client("Test Client", false, "client123");
        Loan loan = new Loan();

        // Act
        loan.setClient(client);

        // Assert
        assertEquals(client, loan.getClient());
    }

    @Test
    public void testLoanInitializationWithoutClient() {
        // Arrange
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, null);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, null);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, null);
        double principalAmount = 50000.0;
        double interestRate = 2.9;
        double commisionRate = 4.0;
        int loanDuration = 12;

        // Act
        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);

        // Assert
        assertEquals(loanAccount, loan.getLoanAccount());
        assertEquals(checkingAccount, loan.getCheckingAccount());
        assertEquals(bankAccount, loan.getBankAccount());
        assertEquals(principalAmount, loan.getPrincipalLoanAmount());
        assertEquals(interestRate, loan.getInterestRate());
        assertEquals(commisionRate, loan.getCommisionRate());
        assertEquals(loanDuration, loan.getLoanDuration());
        assertEquals(null, loan.getClient());
    }

    @Test
    public void testCommisionAmount() {
        // Arrange
        double principalAmount = 10000.0;
        double commisionRate = 3.5;

        Loan loan = new Loan();
        
        // Act
        double calculatedCommision = loan.commisionAmout(principalAmount, commisionRate);
        double expectedCommision = 350.0;

        // Assert
        assertEquals(calculatedCommision, expectedCommision);
    }

    @Test
    public void testInterestAmount() {
        // Arrange
        double principalAmount = 10000.0;
        double interestRate = 8.2;
        int loanDuration = 120;

        Loan loan = new Loan();
        
        // Act
        double calculatedIntrest = loan.intrestAmount(principalAmount, interestRate, loanDuration);
        double expectedIntrest = 8200.0;

        // Assert
        assertEquals(calculatedIntrest, expectedIntrest);
    }

    @Test
    public void testGenerateInstallments() {
        // Arrange
        Client client = new Client("loanTaker", false, "abc123");
        Client bankClient = new Client("bankName", false, "1234");
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, client);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, bankClient);
        double principalAmount = 50000.0;
        double interestRate = 2.9;
        double commisionRate = 4.0;
        int loanDuration = 12;

        // Act
        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setTotalLoanAmout(principalAmount + loan.intrestAmount(principalAmount, interestRate, loanDuration) + loan.commisionAmout(principalAmount, commisionRate));
        loan.generateInstallments();

        // Assert
        assertEquals(loanDuration, loan.getInstallments().size());
        assertThat(loan.getInstallments()).allMatch(installment -> installment.getLoan().equals(loan));
    }
}