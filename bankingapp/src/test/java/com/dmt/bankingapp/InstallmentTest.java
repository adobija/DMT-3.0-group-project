package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
public class InstallmentTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testInstallmentGettersAndSetters() {
        // Arrange
        Loan loan = new Loan();
        double installmentAmount = 1000.0;
        LocalDateTime dueDate = LocalDateTime.now().plusMonths(1);

        // Act
        Installment installment = new Installment(loan, installmentAmount, dueDate);
        installment.setInstallmentID(1);
        installment.setLoan(loan);
        installment.setInstallmentAmount(installmentAmount);
        installment.setDueDate(dueDate);

        // Assert
        assertEquals(1, installment.getInstallmentID());
        assertEquals(loan, installment.getLoan());
        assertEquals(installmentAmount, installment.getInstallmentAmount());
        assertEquals(dueDate, installment.getDueDate());
    }

    @Test
    public void testInstallmentsGenerationInGrantLoan() {
        // Arrange
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, new Client("loanTaker", false, "abc123"));
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, new Client("loanTaker", false, "abc123"));
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, new Client("bankName", false, "1234"));
        double principalAmount = 50000.0;
        double interestRate = 2.9;
        double commisionRate = 4.0;
        int loanDuration = 72;

        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        // Act
        Loan loan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        entityManager.persist(loan);

        Loan foundLoan = entityManager.find(Loan.class, loan.getLoanID());
        List<Installment> foundInstallments = foundLoan.getInstallments();

        // Assert
        assertNotNull(foundInstallments);
        assertEquals(loanDuration, foundInstallments.size());
        double expectedInstallmentAmount = loan.getTotalLoanAmount() / loanDuration;

        for (int i = 0; i < loanDuration; i++) {
            Installment installment = foundInstallments.get(i);
            assertEquals(expectedInstallmentAmount, installment.getInstallmentAmount());
            assertEquals(foundLoan, installment.getLoan());
            assertEquals(loan.getDateOfLoan().plusMonths(i + 1), installment.getDueDate());
        }
    }

    @Test
    public void testPayingInstallments() {
        // Arrange
        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, new Client("loanTaker", false, "abc123"));
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, new Client("loanTaker", false, "abc123"));
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, new Client("bankName", false, "1234"));
        double principalAmount = 10000.0;
        double interestRate = 5.9;
        double commisionRate = 5.0;
        int loanDuration = 60;
 
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        // Act
        Loan testLoan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        entityManager.persist(testLoan);

        loanAccount.setLoan(testLoan);
        entityManager.persist(loanAccount);

        Loan foundLoan = entityManager.find(Loan.class, testLoan.getLoanID());
        List<Installment> foundInstallments = foundLoan.getInstallments();

        double testInstallmentAmount = foundInstallments.get(0).getInstallmentAmount();
        double overpayment = 120.50;
        double paymentThreeInstallments = (testInstallmentAmount * 3) + overpayment;

        Transaction testTransaction = new Transaction(checkingAccount, loanAccount, paymentThreeInstallments);
        entityManager.persist(testTransaction);

        // Assert
        assertNotNull(foundInstallments);
        assertTrue(foundInstallments.get(0).getIsPaid());
        assertTrue(foundInstallments.get(1).getIsPaid());
        assertTrue(foundInstallments.get(2).getIsPaid());
        assertFalse(foundInstallments.get(3).getIsPaid());
        assertEquals(foundInstallments.get(3).getPaidAmount(), overpayment, 0.01);
        assertFalse(foundInstallments.get(4).getIsPaid());
        assertEquals(foundInstallments.get(4).getPaidAmount(), 0, 0.01);
    }

    @Test
    public void testRedeemingAll() {
        // Arrange
        Client loanTaker = new Client("loanTaker", false, "abc123");
        Client bankClient = new Client("bankName", false, "1234");

        entityManager.persist(loanTaker);
        entityManager.persist(bankClient);

        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, loanTaker);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, loanTaker);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, bankClient);
        
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
        List<Installment> foundInstallments = foundLoan.getInstallments();

        // Assert
        assertEquals(foundInstallments.size(), loanDuration);
        boolean allInstallmentsPaidBefore = foundInstallments.stream().allMatch(Installment::getIsPaid);
        assertFalse(allInstallmentsPaidBefore);
        
        Transaction testTransaction = new Transaction(checkingAccount, loanAccount, foundTotalAmount);
        entityManager.persist(testTransaction);
        entityManager.flush();

        boolean allInstallmentsPaidAfter = foundInstallments.stream().allMatch(Installment::getIsPaid);
        assertTrue(allInstallmentsPaidAfter);
    }
}
