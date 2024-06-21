package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.CommissionController;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.repository.CommissionRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;

public class CommissionControllerTests {

    @Mock
    private CommissionRepository commissionRepository;

    @Mock
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @InjectMocks
    private CommissionController commissionController;

    private MockHttpServletRequest request;
    private Client adminClient;
    private Client nonAdminClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        adminClient = new Client("Admin", true, "password");
        nonAdminClient = new Client("User", false, "password");
    }

    @Test
    void testSetCommissionRateForLoanCommissionSuccess() {
        // Arrange
        int newRate = 10;
        Commission commission = new Commission(5, "LOAN_COMMISSION", LocalDateTime.now().minusDays(1));

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(commissionRepository.findByCommissionOf("LOAN_COMMISSION")).thenReturn(commission);

        // Act
        String response = commissionController.setCommissionRateForLoanCommission(newRate, request);

        // Assert
        assertEquals("Successfully updated commission rate for LOANS from 5.0 to 10.0", response);
        verify(commissionRepository, times(2)).save(any(Commission.class)); // Adjusted verification
    }

    @Test
    void testSetCommissionRateForLoanCommissionNotAdmin() {
        // Arrange
        int newRate = 10;

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(nonAdminClient);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commissionController.setCommissionRateForLoanCommission(newRate, request);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("You don't have permission!", exception.getReason());
    }

    @Test
    void testSetCommissionRateForLoanCommissionRateUnchanged() {
        // Arrange
        int newRate = 5;
        Commission commission = new Commission(5, "LOAN_COMMISSION", LocalDateTime.now().minusDays(1));

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(commissionRepository.findByCommissionOf("LOAN_COMMISSION")).thenReturn(commission);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commissionController.setCommissionRateForLoanCommission(newRate, request);
        });

        assertEquals(HttpStatus.IM_USED, exception.getStatusCode());
        assertEquals("New commission rate must be different from previous one!", exception.getReason());
    }

    @Test
    void testSetCommissionRateForLoanCommissionInvalidRate() {
        // Arrange
        int newRate = -1;
        Commission commission = new Commission(5, "LOAN_COMMISSION", LocalDateTime.now().minusDays(1));

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(commissionRepository.findByCommissionOf("LOAN_COMMISSION")).thenReturn(commission);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            commissionController.setCommissionRateForLoanCommission(newRate, request);
        });

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Insert valid new commission rate", exception.getReason());
    }

    @Test
    void testSetCommissionRateForDepositSuccess() {
        // Arrange
        int newRate = 10;
        Commission commission = new Commission(5, "DEPOSIT", LocalDateTime.now().minusDays(1));

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(commissionRepository.findByCommissionOf("DEPOSIT")).thenReturn(commission);

        // Act
        String response = commissionController.setCommissionRateForDeposit(newRate, request);

        // Assert
        assertEquals("Successfully updated commission rate for DEPOSITS from 5.0 to 10.0", response);
        verify(commissionRepository, times(2)).save(any(Commission.class)); // Adjusted verification
    }

    @Test
    void testSetCommissionRateForLoanInterestSuccess() {
        // Arrange
        int newRate = 7;
        Commission commission = new Commission(4, "LOAN_INTEREST", LocalDateTime.now().minusDays(1));

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(adminClient);
        when(commissionRepository.findByCommissionOf("LOAN_INTEREST")).thenReturn(commission);

        // Act
        String response = commissionController.setCommissionRateForLoanInterest(newRate, request);

        // Assert
        assertEquals("Successfully updated interest rate for LOANS from 4.0 to 7.0", response);
        verify(commissionRepository, times(2)).save(any(Commission.class)); // Adjusted verification
    }
}
