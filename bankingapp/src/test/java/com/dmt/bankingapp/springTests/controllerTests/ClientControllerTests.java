package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor; // Add this import statement
import org.springframework.mock.web.MockHttpServletRequest;
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

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEditNameSuccess() {

        // Arrange
        String newName = "New Name";
        String currentName = "Current Name";
        Client client = new Client();
        client.setClientName(currentName);

        // Act
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(currentName);
        when(clientRepository.findByClientName(currentName)).thenReturn(client);
        when(clientRepository.findByClientName(newName)).thenReturn(null);

        String response = clientController.editName(newName, request);

        // Assert
        assertEquals("Client's name updated successfully", response);
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class); // Declare and initialize
        verify(clientRepository).save(clientArgumentCaptor.capture());
        assertEquals(newName, clientArgumentCaptor.getValue().getClientName());
    }
}
