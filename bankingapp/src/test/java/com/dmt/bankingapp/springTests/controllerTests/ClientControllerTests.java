package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor; // Add this import statement
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.ClientController;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

public class ClientControllerTests {

    @Mock
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientController clientController;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
    }

    @Test
    void testEditNameSuccess() {
        // Arrange
        String newName = "New Name";
        String currentName = "Current Name";
        Client client = new Client();
        client.setClientName(currentName);

        // Act
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(currentName);
        when(clientRepository.findByClientName(currentName)).thenReturn(client);
        when(clientRepository.findByClientName(newName)).thenReturn(null);

        String response = clientController.editName(newName, request);

        // Assert
        assertEquals("Client's name updated successfully", response);
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientArgumentCaptor.capture());
        assertEquals(newName, clientArgumentCaptor.getValue().getClientName());
    }

    @Test
    void testEditPasswordClientNotFound() {
        // Arrange
        String clientName = "ClientName";
        String newPassword = "newPassword";

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(clientName);
        when(clientRepository.findByClientName(clientName)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientController.editPassword(newPassword, request);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testEditPermissionRequesterNotAdmin() throws IOException {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, false, "password");
        int clientId = 1;
        boolean isAdmin = true;

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientController.editPermission(clientId, isAdmin, request);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }


    @Test
    void testGetAllClientsRequesterNotAdmin() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, false, "password");

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientController.getAllClients(request);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testGetByClientIDRequesterNotAdmin() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, false, "password");
        int clientId = 1;

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientController.getByClientID(clientId, request);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }
}