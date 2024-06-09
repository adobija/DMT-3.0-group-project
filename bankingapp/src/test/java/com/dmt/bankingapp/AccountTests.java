package com.dmt.bankingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Account.AccountType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class AccountTests {

    @PersistenceContext
    private EntityManager entityManager;
	
    // Test to check whether creating Client and Account features work properly
    @Test
    public void testCreateAccount() {
		// Arrange
		String nameClientOne = "testClient";
        Client client = new Client(nameClientOne, false, "password123");

		String accountNumberOne = "123456789";
        Account account = new Account(accountNumberOne, AccountType.DEPOSIT, client);

        entityManager.persist(client);

		// Act
        Client foundClient = entityManager.find(Client.class, client.getClientID());
		Account foundAccount = entityManager.find(Account.class, account.getAccountID());

		// Assert
        assertThat(foundClient).isNotNull();
		assertThat(foundAccount).isNotNull();

		assertThat(foundClient.getClientName()).isEqualTo(nameClientOne);
        assertFalse(foundClient.isAdmin());
        assertThat(foundAccount.getAccountNumber()).isEqualTo(accountNumberOne);
        assertThat(foundAccount.getAccountBalance()).isEqualTo(0);
    }

    // Test to setter method using 'setAccountBalance' method
	@Test
    public void testSetAccountBalance() {
        // Arrange
        String nameClientOne = "testClient";
        Client client = new Client(nameClientOne, false, "password123");

        String accountNumberOne = "123456789";
        Account account = new Account(accountNumberOne, AccountType.DEPOSIT, client);

        entityManager.persist(client);

        // Act - Update the balance
        Account existingAccount = entityManager.find(Account.class, account.getAccountID());
        Double newBalance = 2000.0;
        existingAccount.setAccountBalance(newBalance, false);
        entityManager.persist(existingAccount);

        // Act - Retrieve the updated account
        Account updatedAccount = entityManager.find(Account.class, account.getAccountID());

        // Assert
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getAccountBalance()).isEqualTo(newBalance);
    }

    @Test
    public void testAssigningLoanToTheLoanAccount() {
        // Arrange
        Client loanTaker = new Client("loanTaker", false, "09876");
        Client bankClient = new Client("loanBank", false, "12345");

        entityManager.persist(loanTaker);
        entityManager.persist(bankClient);

        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, loanTaker);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, loanTaker);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, bankClient);
        
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        double principalAmount = 20000.0;
        double interestRate = 3.8;
        double commisionRate = 5.0;
        int loanDuration = 48;
        Loan testLoan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        entityManager.persist(testLoan);

        // Act
        loanAccount.setLoan(testLoan);
        entityManager.persist(loanAccount);
        Account foundAccount = entityManager.find(Account.class, loanAccount.getAccountID());

        // Assert
        assertThat(foundAccount).isNotNull();
        assertThat(foundAccount.getLoan()).isNotNull();
        assertEquals(foundAccount.getLoan().getLoanID(), testLoan.getLoanID());
    }
}
