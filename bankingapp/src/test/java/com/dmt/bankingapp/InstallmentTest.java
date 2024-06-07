package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;
import com.dmt.bankingapp.entity.Loan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
