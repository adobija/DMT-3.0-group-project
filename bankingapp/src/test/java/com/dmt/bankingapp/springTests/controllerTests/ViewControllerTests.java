package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.ViewController;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.repository.LoanRepository;
import com.dmt.bankingapp.service.implementation.DetailsOfLoggedClientImpl;

public class ViewControllerTests {

    @Mock
    private DetailsOfLoggedClientImpl detailsOfLoggedClient;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ViewController viewController;

    private MockHttpServletRequest request;
    private Client client;
    private Account checkingAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        client = new Client("ClientName", false, "password");
        checkingAccount = new Account("CHK123", Account.AccountType.CHECKING, client);
        checkingAccount.setAccountBalance(1000.0, false);
        client.setCheckingAccount(checkingAccount);
    }

    @Test
    void testShowTransactionForm() {
        // Arrange
        Model model = mock(Model.class);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);

        // Act
        String response = viewController.showTransactionForm(request, model);

        // Assert
        assertEquals("transactionTemplates/makeTransactionForm", response);
        verify(model).addAttribute("clientAccount", "CHK123");
    }

    @Test
    void testViewAdminPanelSuccess() {
        // Arrange
        Model model = mock(Model.class);
        client.setAdmin(true);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);

        // Act
        String response = viewController.viewAdminPanel(request, model);

        // Assert
        assertEquals("adminTemplates/adminPanel", response);
        verify(model).addAttribute("accNumber", "CHK123");
        verify(model).addAttribute("balance", 1000.0);
        verify(model).addAttribute(eq("date"), anyString());
        verify(model).addAttribute("adminInfo", "Welcome to admin panel!");
    }

    @Test
    void testViewAdminPanelForbidden() {
        // Arrange
        Model model = mock(Model.class);
        client.setAdmin(false);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            viewController.viewAdminPanel(request, model);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testShowNextInstallmentForm() {
        // Arrange
        Model model = mock(Model.class);
        Loan loan = new Loan();
        loan.setClient(client);
        List<Loan> loans = List.of(loan);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);
        when(loanRepository.findAll()).thenReturn(loans);

        // Act
        String response = viewController.showNextInstallmentForm(request, model);

        // Assert
        assertEquals("installmentTemplates/nextForm", response);
        verify(model).addAttribute("ClientLoans", loans);
    }

    @Test
    void testShowNextInstallmentFormNoLoans() {
        // Arrange
        Model model = mock(Model.class);
        List<Loan> loans = new ArrayList<>();

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);
        when(loanRepository.findAll()).thenReturn(loans);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            viewController.showNextInstallmentForm(request, model);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
