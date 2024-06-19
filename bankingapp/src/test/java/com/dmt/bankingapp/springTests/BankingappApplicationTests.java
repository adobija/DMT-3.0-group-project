package com.dmt.bankingapp.springTests;

import com.dmt.bankingapp.BankingappApplication;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.CommissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BankingappApplicationTests {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankingappApplication bankingappApplication;

    @Mock
    private CommissionRepository commissionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCommandLineRunnerClientAndAccountsAndCommissionsCreated() throws Exception {
        // Given
        when(clientRepository.findByClientName("BankOwner")).thenReturn(null);
        when(commissionRepository.findByCommissionOf(anyString())).thenReturn(null);

        // When
        CommandLineRunner runner = bankingappApplication.commandLineRunner();
        runner.run();

        // Then
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(commissionRepository, times(2)).save(any(Commission.class));
    }

    @Test
    public void testCommandLineRunnerClientAndAccountsNotDuplicated() throws Exception {
        // Given
        Client existingClient = new Client("BankOwner", true, "dmtprojekt2024");
        when(clientRepository.findByClientName("BankOwner")).thenReturn(existingClient);
        when(commissionRepository.findByCommissionOf("LOAN")).thenReturn(new Commission(10, "LOAN", LocalDateTime.now()));
        when(commissionRepository.findByCommissionOf("DEPOSIT")).thenReturn(new Commission(10, "DEPOSIT", LocalDateTime.now()));

        // When
        CommandLineRunner runner = bankingappApplication.commandLineRunner();
        runner.run();

        // Then
        verify(clientRepository, never()).save(any(Client.class));
        verify(accountRepository, never()).save(any(Account.class));
        verify(commissionRepository, never()).save(any(Commission.class));
    }
}
