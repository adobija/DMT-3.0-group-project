package com.dmt.bankingapp.springTests.entitiesTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Loan;
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
        Account account3 = new Account("087654321", AccountType.LOAN, client);

        entityManager.persist(client);
        entityManager.persist(account1);
        entityManager.persist(account2);
        entityManager.persist(account3);

        // Act - fetch the client from the database
        Client fetchedClient = entityManager.find(Client.class, client.getClientID());

        // Assert - check that the client has two accounts
        assertThat(fetchedClient).isNotNull();
        assertThat(fetchedClient.getAccountsList()).hasSize(3);

        // Assert - Check that the accounts belong to the correct client
        List<Account> accounts = fetchedClient.getAccountsList();
        assertEquals(account1.getAccountNumber(), accounts.get(0).getAccountNumber());
        assertEquals(account2.getAccountNumber(), accounts.get(1).getAccountNumber());
        assertEquals(account3.getAccountNumber(), accounts.get(2).getAccountNumber());
    }

    // Test to check whether it is possible to assign more than one loan to the client
    @Test
    @Transactional
    public void testManyLoansForOneClient() {
        // Arrange - create a new client
        Client client = new Client("testClient", false, "password");
        entityManager.persist(client);

        // Arrange - create accounts needed for the loans
        Account loanAccount1 = new Account("loanAcc1", AccountType.LOAN, client);
        Account checkingAccount1 = new Account("checkAcc1", AccountType.CHECKING, client);
        Account bankAccount1 = new Account("bankAcc1", AccountType.BANK, client);
        entityManager.persist(loanAccount1);
        entityManager.persist(checkingAccount1);
        entityManager.persist(bankAccount1);

        // Arrange - create multiple loans for the client
        Loan loan1 = new Loan(loanAccount1, checkingAccount1, 5000.00, 5.5, 1.5, 24, bankAccount1);
        loan1.setClient(client);
        entityManager.persist(loan1);

        Account loanAccount2 = new Account("loanAcc2", AccountType.LOAN, client);
        Account checkingAccount2 = new Account("checkAcc2", AccountType.CHECKING, client);
        Account bankAccount2 = new Account("bankAcc2", AccountType.BANK, client);
        entityManager.persist(loanAccount2);
        entityManager.persist(checkingAccount2);
        entityManager.persist(bankAccount2);

        Loan loan2 = new Loan(loanAccount2, checkingAccount2, 10000.00, 4.5, 1.0, 36, bankAccount2);
        loan2.setClient(client);
        entityManager.persist(loan2);

        // Act - fetch the client from the database
        Client fetchedClient = entityManager.find(Client.class, client.getClientID());

        // Assert - check that the client has two loans
        assertThat(fetchedClient).isNotNull();
        assertThat(fetchedClient.getLoansList()).hasSize(2);

        // Assert - Check that the loans belong to the correct client
        List<Loan> loans = fetchedClient.getLoansList();
        assertEquals(loan1.getPrincipalLoanAmount(), loans.get(0).getPrincipalLoanAmount());
        assertEquals(loan2.getPrincipalLoanAmount(), loans.get(1).getPrincipalLoanAmount());
    }

    // Test to check whether it is possible to assign more than one deposit to the client
    @Test
    @Transactional
    public void testManyDepositsForOneClient() {
        // Arrange - create a new client
        Client client = new Client("testClient", false, "password");
        entityManager.persist(client);

        // Arrange - create accounts needed for the deposits
        Account depositAccount1 = new Account("checkAcc1", AccountType.DEPOSIT, client);
        entityManager.persist(depositAccount1);

        // Arrange - create multiple deposits for the client
        Deposit deposit1 = new Deposit(5.0, 12, depositAccount1, 2000.00, Deposit.DepositType.FIXED);
        deposit1.setClient(client);
        entityManager.persist(deposit1);

        Account depositAccount2 = new Account("checkAcc2", AccountType.DEPOSIT, client);
        entityManager.persist(depositAccount2);

        Deposit deposit2 = new Deposit(4.0, 24, depositAccount2, 3000.00, Deposit.DepositType.PROGRESSIVE);
        deposit2.setClient(client);
        entityManager.persist(deposit2);

        // Act - fetch the client from the database
        Client fetchedClient = entityManager.find(Client.class, client.getClientID());

        // Assert - check that the client has two deposits
        assertThat(fetchedClient).isNotNull();
        assertThat(fetchedClient.getDepositsList()).hasSize(2);

        // Assert - Check that the deposits belong to the correct client
        List<Deposit> deposits = fetchedClient.getDepositsList();
        assertEquals(deposit1.getTotalDepositAmount(), deposits.get(0).getTotalDepositAmount());
        assertEquals(deposit2.getTotalDepositAmount(), deposits.get(1).getTotalDepositAmount());
    }

    // Test to check feature of getting checking account attributed to the client
    @Test
    @Transactional
    public void testGettingCheckingAccount() {
        // Arrange
        Client client = new Client("testClient", false, "password");
        Account checkingAccount = new Account("checkAcc", AccountType.CHECKING, client);
        entityManager.persist(checkingAccount);
        client.setCheckingAccount(checkingAccount);
        entityManager.persist(client);

        // Act
        Client fetchedClient = entityManager.find(Client.class, client.getClientID());
        Account foundAccount = fetchedClient.getCheckingAccount();

        // Assert
        assertThat(foundAccount).isNotNull();
        assertEquals(checkingAccount.getAccountType(), foundAccount.getAccountType());
        assertEquals(checkingAccount.getAccountNumber(), foundAccount.getAccountNumber());
    }

}
