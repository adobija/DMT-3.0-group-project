package com.dmt.bankingapp.springTests.controllerTests;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import com.dmt.bankingapp.controller.DepositController;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.CommissionRepository;
import com.dmt.bankingapp.repository.DepositRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.interfaceClass.DetailsOfLoggedClient;

class DepositControllerTest {

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
        checkingAccount = new Account();
        bankAccount = new Account();
    }

    @Test
    void testAddNewDepositSuccess() {
        // Set up for success scenario
        client.setCheckingAccount(checkingAccount);
        checkingAccount.setAccountBalance(2000.0, true); // Ensure sufficient funds
        detailsOfLoggedClient = Mockito.mock(DetailsOfLoggedClient.class);

        when(detailsOfLoggedClient.getNameFromClient(request)).thenReturn("ClientName");
        when(clientRepository.findByClientName("ClientName")).thenReturn(client);
        when(accountRepository.findByAccountNumber("BANK_DEPOSIT")).thenReturn(bankAccount);
        List<Deposit> existingDeposits = new ArrayList<>();
        when(depositRepository.getAllByClient(client)).thenReturn(existingDeposits);
        Commission commission = new Commission();
        commission.setCommissionRateInPercent(5);
        when(commissionRepository.findByCommissionOf("DEPOSIT")).thenReturn(commission);

    }

}