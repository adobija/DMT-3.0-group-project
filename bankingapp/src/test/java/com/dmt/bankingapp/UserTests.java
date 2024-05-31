package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

public class UserTests {

    @PersistenceContext
    private EntityManager entityManager;

    // Test to check feature to create a new user with the constructor
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        User user = new User("testUser", true, "password123");

        // Assert
        assertEquals("testUser", user.getUserName());
        assertTrue(user.isAdmin());
        assertTrue(user.getUserPassword().startsWith("{bcrypt}"));
        assertFalse(user.getUserPassword().equals("{bcrypt}password123"));
    }

    // Test to check getters and setters for the entity
    @Test
    public void testSettersAndGetters() {
        // Arrange
        User user = new User();

        // Act
        user.setUserName("newUser");
        user.setAdmin(false);
        user.setUserPassword("newPassword");
        

        // Assert
        assertEquals("newUser", user.getUserName());
        assertFalse(user.isAdmin());
        assertNotNull(user.getUserPassword());
    }

    // Test to verify whether method 'createBcryptHashedPassword' used in 'setUserPassword' works properly
    @Test
    public void testCreateBcryptHashedPassword() {
        // Arrange
        String inputPassword = "password123";
        User user = new User();
        
        // Act
        user.setUserPassword(inputPassword);
        String hashedPassword = user.getUserPassword();
        
        // Assert
        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword).startsWith("{bcrypt}");
        String extractedHash = hashedPassword.substring("{bcrypt}".length());
        assertTrue(BCrypt.checkpw(inputPassword, extractedHash));
    }

    // Test to check whether it is possible to assign more than one account to the user
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
