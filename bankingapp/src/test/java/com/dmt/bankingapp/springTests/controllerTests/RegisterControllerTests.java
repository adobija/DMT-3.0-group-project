package com.dmt.bankingapp.springTests.controllerTests;

import com.dmt.bankingapp.controller.RegisterController;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegisterControllerTests {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private Model model;

    @Test
    void testAddNewClientSuccess() {
        // Arrange
        String clientName = "newClient";
        String clientPassword = "password123";
        when(clientRepository.findByClientName(clientName)).thenReturn(null);
        Account account = new Account("CHK12345", Account.AccountType.CHECKING, new Client());
        when(accountService.addNewAccount(eq(Account.AccountType.CHECKING), any(Client.class))).thenReturn("Account created successfully");
        when(accountService.getLatestAccount()).thenReturn(account);
        when(model.getAttribute("output")).thenReturn("New client profile created successfully");

        // Act
        String response = registerController.addNewClient(clientName, clientPassword, model);

        // Assert
        assertEquals(model.getAttribute("output"), "New client profile created successfully");
        assertEquals("registerTemplates/success", response);
        verify(clientRepository, times(2)).save(any(Client.class));
        verify(accountService, times(1)).addNewAccount(eq(Account.AccountType.CHECKING), any(Client.class));
    }

    @Test
    void testAddNewClientAlreadyExists() {
        // Arrange
        String clientName = "existingClient";
        String clientPassword = "password123";
        Client existingClient = new Client(clientName, false, clientPassword);
        when(clientRepository.findByClientName(clientName)).thenReturn(existingClient);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            registerController.addNewClient(clientName, clientPassword, model);
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Client with this user name already exists - failed to create new client profile", exception.getReason());
        verify(clientRepository, times(0)).save(any(Client.class));
        verify(accountService, times(0)).addNewAccount(eq(Account.AccountType.CHECKING), any(Client.class));
    }
}
