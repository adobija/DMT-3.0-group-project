package com.dmt.bankingapp.springTests.repositoryTests;

import com.dmt.bankingapp.entity.Deposit;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.DepositRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DepositRepositoryTests {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void depositRepositoryTestInsertion() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);

        entityManager.persist(client);
        entityManager.persist(checkingAccount);

        Deposit deposit = new Deposit(10, 12, checkingAccount, 5000.00, Deposit.DepositType.FIXED);
        deposit.setDateOfDeposit(LocalDateTime.now());

        // act
        Deposit savedDeposit = depositRepository.save(deposit);

        // assert
        assertThat(entityManager.find(Deposit.class, savedDeposit.getDepositID())).isEqualTo(deposit);
    }

    @Test
    @Transactional
    public void depositRepositoryTestUpdate() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);

        entityManager.persist(client);
        entityManager.persist(checkingAccount);

        Deposit deposit = new Deposit(10, 12, checkingAccount, 5000.00, Deposit.DepositType.FIXED);
        deposit.setDateOfDeposit(LocalDateTime.now());

        entityManager.persist(deposit);

        double newTotalAmount = 7000.00;

        // act
        deposit.setTotalDepositAmount(newTotalAmount);
        depositRepository.save(deposit);

        // assert
        assertThat(entityManager.find(Deposit.class, deposit.getDepositID()).getTotalDepositAmount()).isEqualTo(newTotalAmount);
    }

    @Test
    @Transactional
    public void depositRepositoryTestDelete() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);

        entityManager.persist(client);
        entityManager.persist(checkingAccount);

        Deposit deposit = new Deposit(10, 12, checkingAccount, 5000.00, Deposit.DepositType.FIXED);
        deposit.setDateOfDeposit(LocalDateTime.now());

        entityManager.persist(deposit);

        // act
        depositRepository.delete(deposit);

        // assert
        assertThat(entityManager.find(Deposit.class, deposit.getDepositID())).isNull();
    }

    @Test
    @Transactional
    public void depositRepositoryTestFindById() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);

        entityManager.persist(client);
        entityManager.persist(checkingAccount);

        Deposit deposit = new Deposit(10, 12, checkingAccount, 5000.00, Deposit.DepositType.FIXED);
        deposit.setDateOfDeposit(LocalDateTime.now());

        entityManager.persist(deposit);

        // act
        Optional<Deposit> foundDeposit = depositRepository.findById(deposit.getDepositID());

        // assert
        assertThat(foundDeposit)
                .isNotNull()
                .contains(deposit);
        assertEquals(deposit.getCheckingAccount(), foundDeposit.get().getCheckingAccount());
    }
}