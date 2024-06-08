package com.dmt.bankingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Account.AccountType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


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
}
