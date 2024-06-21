package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import com.dmt.bankingapp.controller.HistoryController;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.record.History;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.implementation.AccountsOfUserIntoHistoryListImpl;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;

public class HistoryControllerTests {

    @Mock
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountsOfUserIntoHistoryListImpl accountsOfUserIntoHistoryList;

    @InjectMocks
    private HistoryController historyController;

    private MockHttpServletRequest request;
    private Client adminClient;
    private Client nonAdminClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        adminClient = new Client("Admin", true, "password");
        adminClient.setClientID(1);
        nonAdminClient = new Client("User", false, "password");
        nonAdminClient.setClientID(2);
    }

    @Test
    void testGetHistoryOfIncomeAndExpenseSuccess() {
        // Arrange
        ArrayList<History> historyList = new ArrayList<>();
        ResponseEntity<ArrayList<History>> expectedResponse = new ResponseEntity<>(historyList, HttpStatus.OK);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(nonAdminClient);
        when(accountsOfUserIntoHistoryList.getStoredHistoryByClient(nonAdminClient)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ArrayList<History>> response = historyController.getHistoryOfIncomeAndExpense(request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetHistoryOfAccountByAccountNumberAdminOnlySuccess() {
        // Arrange
        int clientId = 2;
        ArrayList<History> historyList = new ArrayList<>();
        ResponseEntity<ArrayList<History>> expectedResponse = new ResponseEntity<>(historyList, HttpStatus.OK);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(nonAdminClient));
        when(accountsOfUserIntoHistoryList.getStoredHistoryByClient(nonAdminClient)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ArrayList<History>> response = historyController.getHistoryOfAccountByAccountNumberAdminOnly(clientId, request);

        // Assert
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetHistoryOfAccountByAccountNumberAdminOnlyNotAdmin() {
        // Arrange
        int clientId = 2;

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(nonAdminClient);

        // Act
        ResponseEntity<ArrayList<History>> response = historyController.getHistoryOfAccountByAccountNumberAdminOnly(clientId, request);

        // Assert
        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You don't have enough permission to perform this action!", headers.getFirst("ErrorMessage"));
    }

    @Test
    void testGetHistoryOfAccountByAccountNumberAdminOnlyClientNotFound() {
        // Arrange
        int clientId = 3;

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ArrayList<History>> response = historyController.getHistoryOfAccountByAccountNumberAdminOnly(clientId, request);

        // Assert
        HttpHeaders headers = response.getHeaders();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Client with id " + clientId + " does not exist!", headers.getFirst("ErrorMessage"));
    }
}
