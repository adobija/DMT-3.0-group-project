package com.dmt.bankingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class BankingappApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;
	
    // Test to check whether creating User and Account features work properly
    @Test
    public void testCreateAccount() {
		// Arrange
		String nameUserOne = "testUser";
        User user = new User(nameUserOne, false, "password123");

		String accountNumberOne = "123456789";
        Account account = new Account(accountNumberOne, "savings", user);

        entityManager.persist(user);

		// Act
        User foundUser = entityManager.find(User.class, user.getUserID());
		Account foundAccount = entityManager.find(Account.class, account.getAccountID());

		// Assert
        assertThat(foundUser).isNotNull();
		assertThat(foundAccount).isNotNull();

		assertThat(foundUser.getUserName()).isEqualTo(nameUserOne);
        assertFalse(foundUser.isAdmin());
        assertThat(foundAccount.getAccountNumber()).isEqualTo(accountNumberOne);
        assertThat(foundAccount.getAccountBalance()).isEqualTo(0);
    }

    // Test to setter method using 'setAccountBalance' method
	@Test
    public void testSetAccountBalance() {
        // Arrange
        String nameUserOne = "testUser";
        User user = new User(nameUserOne, false, "password123");

        String accountNumberOne = "123456789";
        Account account = new Account(accountNumberOne, "savings", user);

        entityManager.persist(user);

        // Act - Update the balance
        Account existingAccount = entityManager.find(Account.class, account.getAccountID());
        Double newBalance = 2000.0;
        existingAccount.setAccountBalance(newBalance);
        entityManager.persist(existingAccount);

        // Act - Retrieve the updated account
        Account updatedAccount = entityManager.find(Account.class, account.getAccountID());

        // Assert
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getAccountBalance()).isEqualTo(newBalance);
    }

    @Test
    @Transactional
    public void testManyAccountsForOneUser() {
        // Arrange - create a new user
        User user = new User("testUser", false, "password");

        // Arrange - create multiple accounts for the user
        Account account1 = new Account("123456789", "checking", user);
        Account account2 = new Account("987654321", "savings", user);

        entityManager.persist(user);

        // Act  - fetch the user from the database
        User fetchedUser = entityManager.find(User.class, user.getUserID());

        // Assert - check that the user has two accounts
        assertThat(fetchedUser).isNotNull();
        assertThat(fetchedUser.getAccountsList()).hasSize(2);

        // Assert - Check that the accounts belong to the correct user
        List<Account> accounts = fetchedUser.getAccountsList();
        assertThat(accounts.get(0).getAccountNumber().equals(account1.getAccountNumber()));
        assertThat(accounts.get(1).getAccountNumber().equals(account2.getAccountNumber()));
    }
}
