package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.LoanController;
import com.dmt.bankingapp.entity.*;
import com.dmt.bankingapp.repository.*;
import com.dmt.bankingapp.service.AccountService;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

public class LoanControllerTests {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @Mock
    private CommissionRepository commissionRepository;

    @InjectMocks
    private LoanController loanController;

    private MockHttpServletRequest request;
    private Client client;
    private Account checkingAccount;
    private Account bankAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        client = new Client("ClientName", false, "password");
        checkingAccount = new Account("CHK123", Account.AccountType.CHECKING, client);
        bankAccount = new Account("BANK_LOAN", Account.AccountType.BANK, null);

        client.setCheckingAccount(checkingAccount);
    }

    @Test
    void testAddNewLoanSuccess() {
        // Arrange
        double principalAmount = 10000.0;
        int loanDuration = 12;
        Model model = mock(Model.class);

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn("ClientName");
        when(clientRepository.findByClientName("ClientName")).thenReturn(client);
        when(accountRepository.findByAccountNumber("BANK_LOAN")).thenReturn(bankAccount);
        when(accountService.addNewAccount(Account.AccountType.LOAN, client)).thenReturn("Account created successfully");
        Account loanAccount = new Account("LOAN123", Account.AccountType.LOAN, client);
        when(accountService.getLatestAccount()).thenReturn(loanAccount);
        Commission loanCommission = new Commission();
        loanCommission.setCommissionRateInPercent(5.0);
        when(commissionRepository.findByCommissionOf("LOAN_COMMISSION")).thenReturn(loanCommission);
        Commission loanInterest = new Commission();
        loanInterest.setCommissionRateInPercent(3.0);
        when(commissionRepository.findByCommissionOf("LOAN_INTEREST")).thenReturn(loanInterest);

        // Act
        String response = loanController.addNewLoan(principalAmount, loanDuration, request, model);

        // Assert
        assertEquals("loanTemplates/add", response);
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(transactionRepository, times(3)).save(any(Transaction.class));
        verify(model).addAttribute("add", "Loan and loan account created successfully");
    }

    @Test
    void testGetAllLoansRequesterNotAdmin() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, false, "password");

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanController.getAllLoans(request, mock(Model.class));
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testGetLoanByIdRequesterNotAdmin() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, false, "password");
        int loanId = 1;

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            loanController.getLoanById(loanId, request,mock(Model.class));
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void testGetLoanByIdLoanNotFound() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, true, "password"); // Admin requester
        int loanId = 1;

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);
        when(loanRepository.findByLoanID(loanId)).thenReturn(null);

        // Act and Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            loanController.getLoanById(loanId, request, mock(Model.class));
        });

        assertEquals("Loan with this ID does not exist!", exception.getMessage());
    }

    @Test
    void testGetAllLoansSuccess() {
        // Arrange
        String requesterName = "Requester";
        Client requester = new Client(requesterName, true, "password"); // Admin requester
        requester.setClientID(1);
        Model model = mock(Model.class);

        Client client = new Client("ClientName", false, "password");
        client.setClientID(2);

        Loan loan = new Loan();
        loan.setLoanID(1);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setLoanAccount(new Account("LOAN123", Account.AccountType.LOAN, client));
        loan.setTotalLoanAmout(10000.0);
        loan.setLeftToPay(5000.0);
        loan.setClient(client);

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn(requesterName);
        when(clientRepository.findByClientName(requesterName)).thenReturn(requester);
        when(loanRepository.findAll()).thenReturn(List.of(loan));

        // Act
        String response = loanController.getAllLoans(request, model);

        // Assert
        assertEquals("loanTemplates/allLoans", response);
        verify(model).addAttribute("all", "1: " + loan.getDateOfLoan().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                    "  account: LOAN123  total: 10000.0  left: 5000.0  client: 2");
    }
}
