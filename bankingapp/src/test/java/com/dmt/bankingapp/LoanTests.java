package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Transactional
public class LoanTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testLoanConstructor() {
        // Arrange
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, new Client("loanTaker", false, "abc123"));
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, new Client("loanTaker", false, "abc123"));
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, new Client("bankName", false, "1234"));
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
        assertEquals(interestRate, loan.getIntrestRate());
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
        loan.setIntrestRate(interestRate);
        loan.setCommisionRate(commisionRate);
        loan.setDateOfLoan(timestamp);

        // Assert
        assertEquals(loanID, loan.getLoanID());
        assertEquals(principalAmount, loan.getPrincipalLoanAmount());
        assertEquals(totalAmount, loan.getTotalLoanAmount());
        assertEquals(loanDuration, loan.getLoanDuration());
        assertEquals(interestRate, loan.getIntrestRate());
        assertEquals(commisionRate, loan.getCommisionRate());
        assertEquals(timestamp, loan.getDateOfLoan());
    }

    @Test
    public void testIntrestAmount() {
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
    public void testGrantLoan() {
        // Arrange - instantiating a client and a bank, instantiating checking and loan account for the client, and account for the bank
        String loanTaker = "clientTaker";
        String bankName = "bank";

        Client client1 = new Client(loanTaker, false, "abc123");
        Client bank = new Client(bankName, false, "1234");

        String checkingAccNum = "33311100";
        String loanAccNum = "44422211";
        String bankAccNum = "00000000";

        Account checkingAccTest = new Account(checkingAccNum, AccountType.CHECKING, client1);
        Account loanAccTest = new Account(loanAccNum, AccountType.LOAN, client1);
        Account bankAccTest = new Account(bankAccNum, AccountType.BANK, bank);


        double checkingInitialBalace = 255.90;
        double bankInitialBalance = 999999.99;
        checkingAccTest.setAccountBalance(checkingInitialBalace, false);
        bankAccTest.setAccountBalance(bankInitialBalance, false);

        entityManager.persist(client1);
        entityManager.persist(bank);
        entityManager.persist(checkingAccTest);
        entityManager.persist(loanAccTest);
        entityManager.persist(bankAccTest);

        // Act - arranging new loan and variables required to grant the loan
        double testLoanAmount = 10000.0;
        double testIntrestRate = 5.7;
        double testCommisionRate = 4.0;
        int testLoanDuration = 60;
        Loan testLoan = new Loan(loanAccTest, checkingAccTest, testLoanAmount, testIntrestRate, testCommisionRate, testLoanDuration, bankAccTest);
        entityManager.persist(testLoan);

        Account foundLoanAccount = entityManager.find(Account.class, loanAccTest.getAccountID());
        Account foundCheckingAccount = entityManager.find(Account.class, checkingAccTest.getAccountID());
        Account foundBankAccount = entityManager.find(Account.class, bankAccTest.getAccountID());

        // Assert - checking whether loan is launched and money correctly transfered to checking account and bank account
        assertThat(foundCheckingAccount).isNotNull();
        assertThat(foundLoanAccount).isNotNull();
        assertThat(foundBankAccount).isNotNull();

        double testCheckingAccBalance = checkingInitialBalace + testLoanAmount;
        assertEquals(testCheckingAccBalance, foundCheckingAccount.getAccountBalance());

        double testLoanAccBalance = -testLoanAmount - testLoan.intrestAmount(testLoanAmount, testIntrestRate, testLoanDuration) - testLoan.commisionAmout(testLoanAmount, testCommisionRate);
        assertEquals(testLoanAccBalance, foundLoanAccount.getAccountBalance());

        double testBankAccBalance = bankInitialBalance + testLoan.intrestAmount(testLoanAmount, testIntrestRate, testLoanDuration) + testLoan.commisionAmout(testLoanAmount, testCommisionRate);
        assertEquals(testBankAccBalance, foundBankAccount.getAccountBalance());
    }

    @Test
    public void liquidateLoan() {
        // Arrange
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, new Client("loanTaker", false, "abc123"));
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, new Client("loanTaker", false, "abc123"));
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, new Client("bankName", false, "1234"));
        double checkingAccountBalance = 10000.0;
        checkingAccount.setAccountBalance(checkingAccountBalance, false);
        double principalAmount = 5000.0;
        double interestRate = 7.1;
        double commisionRate = 5.0;
        int loanDuration = 36;
  
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        // Act
        Loan testLoan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        entityManager.persist(testLoan);

        loanAccount.setLoan(testLoan);
        entityManager.persist(loanAccount);

        Loan foundLoan = entityManager.find(Loan.class, testLoan.getLoanID());
        double foundTotalAmount = foundLoan.getTotalLoanAmount();

        // Assert
        assertThat(foundLoan).isNotNull();
        assertTrue(foundLoan.getIsActive());

        Transaction testTransaction = new Transaction(checkingAccount, loanAccount, foundTotalAmount);
        entityManager.persist(testTransaction);

        // assertFalse(foundLoan.getIsActive());
    }
}
