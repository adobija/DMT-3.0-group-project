package com.dmt.bankingapp.springTests.entitiesTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Account.AccountType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClientTests {

    @PersistenceContext
    private EntityManager entityManager;

    // Test to check feature to create a new client with the constructor
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        Client client = new Client("testClient", true, "password123");

        // Assert
        assertEquals("testClient", client.getClientName());
        assertTrue(client.isAdmin());
        assertTrue(client.getClientPassword().startsWith("{bcrypt}"));
        assertFalse(client.getClientPassword().equals("{bcrypt}password123"));
    }

    // Test to check getters and setters for the entity
    @Test
    public void testSettersAndGetters() {
        // Arrange
        Client client = new Client();

        // Act
        client.setClientName("newClient");
        client.setAdmin(false);
        client.setClientPassword("newPassword");

        // Assert
        assertEquals("newClient", client.getClientName());
        assertFalse(client.isAdmin());
        assertNotNull(client.getClientPassword());
    }

    // Test to verify whether method 'createBcryptHashedPassword' used in 'setClientPassword' works properly
    @Test
    public void testCreateBcryptHashedPassword() {
        // Arrange
        String inputPassword = "password123";
        Client client = new Client();

        // Act
        client.setClientPassword(inputPassword);
        String hashedPassword = client.getClientPassword();

        // Assert
        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword).startsWith("{bcrypt}");
        String extractedHash = hashedPassword.substring("{bcrypt}".length());
        assertTrue(BCrypt.checkpw(inputPassword, extractedHash));
    }

    // Test to check whether it is possible to assign more than one account to the client
    @Test
    @Transactional
    public void testManyAccountsForOneClient() {
        // Arrange - create a new client
        Client client = new Client("testClient", false, "password");

        // Arrange - create multiple accounts for the client
        Account account1 = new Account("123456789", AccountType.CHECKING, client);
        Account account2 = new Account("987654321", AccountType.DEPOSIT, client);

        entityManager.persist(client);
        entityManager.persist(account1);
        entityManager.persist(account2);

        // Act - fetch the client from the database
        Client fetchedClient = entityManager.find(Client.class, client.getClientID());

        // Assert - check that the client has two accounts
        assertThat(fetchedClient).isNotNull();
        assertThat(fetchedClient.getAccountsList()).hasSize(2);

        // Assert - Check that the accounts belong to the correct client
        List<Account> accounts = fetchedClient.getAccountsList();
        assertEquals(account1.getAccountNumber(), accounts.get(0).getAccountNumber());
        assertEquals(account2.getAccountNumber(), accounts.get(1).getAccountNumber());
    }
}
