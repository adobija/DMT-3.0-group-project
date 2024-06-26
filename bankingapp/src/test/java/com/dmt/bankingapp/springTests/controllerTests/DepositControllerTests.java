package com.dmt.bankingapp.springTests.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.dmt.bankingapp.controller.DepositController;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Deposit.DepositType;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.CommissionRepository;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

class DepositControllerTests {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private DepositRepository depositRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CommissionRepository commissionRepository;
    @Mock
    private DetailsOfLoggedClient detailsOfLoggedClient;

    @InjectMocks
    private DepositController depositController;

    private MockHttpServletRequest request;
    private Client client;
    private Account checkingAccount;
    private Account bankAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        request = new MockHttpServletRequest();
        client = new Client();
        checkingAccount = new Account("CHK123", Account.AccountType.CHECKING, client);
        bankAccount = new Account("BANK_DEPOSIT", Account.AccountType.BANK, null);

        client.setCheckingAccount(checkingAccount);
    }

    @Test
    void testAddNewDepositSuccess() {
        // Arrange
        checkingAccount.setAccountBalance(2000.0, false);

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn("ClientName");
        when(clientRepository.findByClientName("ClientName")).thenReturn(client);
        when(accountRepository.findByAccountNumber("BANK_DEPOSIT")).thenReturn(bankAccount);
        List<Deposit> existingDeposits = new ArrayList<>();
        when(depositRepository.getAllByClient(client)).thenReturn(existingDeposits);

        // Act
        Commission commission = new Commission();
        commission.setCommissionRateInPercent(5);
        when(commissionRepository.findByCommissionOf("DEPOSIT")).thenReturn(commission);

        String response = depositController.addNewDeposit(500.0, 12, "FIXED", request, mock(Model.class));

        // Assert
        assertEquals("indexTemplates/hello", response);
        verify(depositRepository, times(1)).save(any(Deposit.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdrawDepositSuccess() {
        // Arrange
        checkingAccount.setAccountBalance(2000.0, false);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);
        when(accountRepository.findByAccountNumber("CHK123")).thenReturn(checkingAccount);
        when(accountRepository.findByAccountNumber("BANK_DEPOSIT")).thenReturn(bankAccount);

        // Act
        Deposit deposit = new Deposit();
        deposit.setDepositType(DepositType.FIXED);
        deposit.setActive(true);
        deposit.setDateOfDeposit(LocalDateTime.now().minusMonths(12)); // Example date
        deposit.setDepositDuration(12); // Example duration
        deposit.setTotalDepositAmount(1000.0); // Example amount
        deposit.setReturnOfInvestment(1100.0); // Example return of investment

        List<Deposit> existingDeposits = new ArrayList<>();
        existingDeposits.add(deposit); // Add the deposit to the list

        when(depositRepository.getAllByClient(client)).thenReturn(existingDeposits);

        String response = depositController.withdrawDeposit(request, deposit, mock(Model.class));

        // Assert
        assertEquals("indexTemplates/hello", response);
        verify(depositRepository, times(1)).save(any(Deposit.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdrawDepositLocked() {
        // Arrange
        checkingAccount.setAccountBalance(2000.0, false);

        when(detailsOfLoggedClient.getLoggedClientInstance(request)).thenReturn(client);
        when(accountRepository.findByAccountNumber("CHK123")).thenReturn(checkingAccount);
        when(accountRepository.findByAccountNumber("BANK_DEPOSIT")).thenReturn(bankAccount);

        // Act
        Deposit deposit = new Deposit();
        deposit.setDepositType(DepositType.FIXED);
        deposit.setActive(true);
        deposit.setDateOfDeposit(LocalDateTime.now().minusMonths(6)); // Example date, less than 12 months ago
        deposit.setDepositDuration(12); // Example duration
        deposit.setTotalDepositAmount(1000.0); // Example amount
        deposit.setReturnOfInvestment(1100.0); // Example return of investment

        List<Deposit> existingDeposits = new ArrayList<>();
        existingDeposits.add(deposit); // Add the deposit to the list

        when(depositRepository.getAllByClient(client)).thenReturn(existingDeposits);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            depositController.withdrawDeposit(request, deposit, mock(Model.class))
        );

        // Assert
        assertEquals("You cannot withdraw money from that deposit until " + deposit.getDateOfDeposit().plusMonths(deposit.getDepositDuration()) + "!", exception.getReason());
    }
}
