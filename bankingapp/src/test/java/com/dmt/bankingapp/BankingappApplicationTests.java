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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class BankingappApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;
	
    @Test
    public void testCreateAccount() {
		// Arrange
		String nameUserOne = "testUser";
        User user = new User(nameUserOne, false, "password123");
        entityManager.persist(user);

		String accountNumberOne = "123456789";
		double balanceOne = 1000.0;
        Account account = new Account(accountNumberOne, balanceOne, "savings", user);
        entityManager.persist(account);

		// Act
        User foundUser = entityManager.find(User.class, user.getUserID());
		Account foundAccount = entityManager.find(Account.class, account.getAccountID());

		// Assert
        assertThat(foundUser).isNotNull();
		assertThat(foundAccount).isNotNull();

		assertThat(foundUser.getUserName()).isEqualTo(nameUserOne);
        assertFalse(foundUser.isAdmin());
        assertThat(foundAccount.getAccount_number()).isEqualTo(accountNumberOne);
        assertThat(foundAccount.getAccount_balance()).isEqualTo(balanceOne);
    }

	@Test
    public void testSetAccountBalance() {
        // Arrange
        String nameUserOne = "testUser";
        User user = new User(nameUserOne, false, "password123");
        entityManager.persist(user);

        String accountNumberOne = "123456789";
        Double initialBalance = 1000.0;
        Account account = new Account(accountNumberOne, initialBalance, "savings", user);
        entityManager.persist(account);

        // Act - Update the balance
        Account existingAccount = entityManager.find(Account.class, account.getAccountID());
        Double newBalance = 2000.0;
        existingAccount.setAccount_balance(newBalance);
        entityManager.persist(existingAccount);

        // Act - Retrieve the updated account
        Account updatedAccount = entityManager.find(Account.class, account.getAccountID());

        // Assert
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getAccount_balance()).isEqualTo(newBalance);
    }
}
