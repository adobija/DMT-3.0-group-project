package com.dmt.bankingapp.springTests.serviceTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client("Test Client", false, "password");
        client.setClientID(1);
    }

    @Test
    void testAddNewAccountClientNotFound() {
        // Act
        String response = accountService.addNewAccount(Account.AccountType.CHECKING, null);

        // Assert
        assertEquals("Client not found", response);
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void testGetLatestAccount() {
        // Arrange
        Account account = new Account("CHK12345", Account.AccountType.CHECKING, client);
        when(accountRepository.findTopByOrderByAccountIDDesc()).thenReturn(account);

        // Act
        Account latestAccount = accountService.getLatestAccount();

        // Assert
        assertEquals(account, latestAccount);
    }

    @Test
    void testGetAllAccounts() {
        // Arrange
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("CHK12345", Account.AccountType.CHECKING, client));
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> allAccounts = accountService.getAllAccounts();

        // Assert
        assertEquals(1, allAccounts.size());
        assertEquals("CHK12345", allAccounts.get(0).getAccountNumber());
    }

    @Test
    void testGetAccountByNumber() {
        // Arrange
        Account account = new Account("CHK12345", Account.AccountType.CHECKING, client);
        when(accountRepository.findByAccountNumber("CHK12345")).thenReturn(account);

        // Act
        Account foundAccount = accountService.getAccountByNumber("CHK12345");

        // Assert
        assertEquals(account, foundAccount);
    }

    @Test
    void testGetAccountByNumberNotFound() {
        // Arrange
        when(accountRepository.findByAccountNumber("CHK12345")).thenReturn(null);

        // Act
        Account foundAccount = accountService.getAccountByNumber("CHK12345");

        // Assert
        assertNull(foundAccount);
    }
}
