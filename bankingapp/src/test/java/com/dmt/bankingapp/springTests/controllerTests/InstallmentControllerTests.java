package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.InstallmentController;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Installment;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.InstallmentRepository;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

public class InstallmentControllerTests {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private InstallmentRepository installmentRepository;

    @Mock
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @InjectMocks
    private InstallmentController installmentController;

    private MockHttpServletRequest request;
    private Client adminClient;
    private Client nonAdminClient;
    private Loan activeLoan;
    private Installment unpaidInstallment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        adminClient = new Client("Admin", true, "password");
        adminClient.setClientID(1);
        nonAdminClient = new Client("User", false, "password");
        nonAdminClient.setClientID(2);

        activeLoan = new Loan();
        activeLoan.setLoanID(1);
        activeLoan.setClient(nonAdminClient);
        activeLoan.setIsActive(true);

        unpaidInstallment = new Installment();
        unpaidInstallment.setInstallmentID(1);
        unpaidInstallment.setLoan(activeLoan);
        unpaidInstallment.setInstallmentAmount(500.0);
        unpaidInstallment.setPaidAmount(0.0);
        unpaidInstallment.setIsPaid(false);
        unpaidInstallment.setDueDate(LocalDateTime.now().plusDays(30));

        activeLoan.getInstallments().add(unpaidInstallment);
        nonAdminClient.getLoansList().add(activeLoan);
    }

    @Test
    void testGetMyInstallmentsSuccess() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(nonAdminClient.getClientName());
        when(clientRepository.findByClientName(nonAdminClient.getClientName())).thenReturn(nonAdminClient);
        Model mockModel = mock(Model.class);

        // Act
        String viewName = installmentController.getMyInstallments(request, mockModel);

        // Assert
        verify(mockModel, times(1)).addAttribute(eq("myAll"), anyString());
        assertEquals("installmentTemplates/myAll", viewName);
    }

    @Test
    void testGetNextInstallmentSuccess() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(nonAdminClient.getClientName());
        when(clientRepository.findByClientName(nonAdminClient.getClientName())).thenReturn(nonAdminClient);
        when(loanRepository.findByLoanID(1)).thenReturn(activeLoan);
        Model mockModel = mock(Model.class);

        // Act
        String viewName = installmentController.getNextInstallment(1, request, mockModel);

        // Assert
        verify(mockModel, times(1)).addAttribute(eq("next"), anyString());
        assertEquals("installmentTemplates/next", viewName);
    }

    @Test
    void testGetNextInstallmentLoanNotFound() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(nonAdminClient.getClientName());
        when(clientRepository.findByClientName(nonAdminClient.getClientName())).thenReturn(nonAdminClient);
        when(loanRepository.findByLoanID(1)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            installmentController.getNextInstallment(1, request, mock(Model.class));
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Loan has not been found", exception.getReason());
    }

    @Test
    void testGetNextInstallmentForbidden() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(adminClient.getClientName());
        when(clientRepository.findByClientName(adminClient.getClientName())).thenReturn(adminClient);
        when(loanRepository.findByLoanID(1)).thenReturn(activeLoan);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            installmentController.getNextInstallment(1, request, mock(Model.class));
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("You don't have permission!", exception.getReason());
    }

    @Test
    void testGetGivenInstallmentSuccess() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(adminClient.getClientName());
        when(clientRepository.findByClientName(adminClient.getClientName())).thenReturn(adminClient);
        when(installmentRepository.findByInstallmentID(1)).thenReturn(unpaidInstallment);
        Model mockModel = mock(Model.class);

        // Act
        String viewName = installmentController.getGivenInstallment(1, request, mockModel);

        // Assert
        verify(mockModel, times(1)).addAttribute(eq("given"), anyString());
        assertEquals("installmentTemplates/given", viewName);
    }

    @Test
    void testGetGivenInstallmentForbidden() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(nonAdminClient.getClientName());
        when(clientRepository.findByClientName(nonAdminClient.getClientName())).thenReturn(nonAdminClient);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            installmentController.getGivenInstallment(1, request, mock(Model.class));
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("You don't have permission!", exception.getReason());
    }

    @Test
    void testGetLoanInstallmentsSuccess() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(adminClient.getClientName());
        when(clientRepository.findByClientName(adminClient.getClientName())).thenReturn(adminClient);
        when(loanRepository.findByLoanID(1)).thenReturn(activeLoan);
        Model mockModel = mock(Model.class);

        // Act
        String viewName = installmentController.getLoanInstallments(1, request, mockModel);

        // Assert
        verify(mockModel, times(1)).addAttribute(eq("loan"), anyString());
        assertEquals("installmentTemplates/loan", viewName);
    }

    @Test
    void testGetLoanInstallmentsLoanNotFound() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(adminClient.getClientName());
        when(clientRepository.findByClientName(adminClient.getClientName())).thenReturn(adminClient);
        when(loanRepository.findByLoanID(1)).thenReturn(null);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            installmentController.getLoanInstallments(1, request, mock(Model.class));
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Loan has not been found", exception.getReason());
    }

    @Test
    void testGetAllInstallmentsSuccess() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(adminClient.getClientName());
        when(clientRepository.findByClientName(adminClient.getClientName())).thenReturn(adminClient);
        when(installmentRepository.findAll()).thenReturn(Arrays.asList(unpaidInstallment));
        Model mockModel = mock(Model.class);

        // Act
        String viewName = installmentController.getAllInstallments(request, mockModel);

        // Assert
        verify(mockModel, times(1)).addAttribute(eq("all"), anyString());
        assertEquals("installmentTemplates/all", viewName);
    }

    @Test
    void testGetAllInstallmentsForbidden() {
        // Arrange
        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(nonAdminClient.getClientName());
        when(clientRepository.findByClientName(nonAdminClient.getClientName())).thenReturn(nonAdminClient);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            installmentController.getAllInstallments(request, mock(Model.class));
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("You don't have permission!", exception.getReason());
    }
}
