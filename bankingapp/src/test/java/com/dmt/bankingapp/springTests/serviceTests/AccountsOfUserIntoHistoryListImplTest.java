package com.dmt.bankingapp.springTests.serviceTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.record.History;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import com.dmt.bankingapp.service.implementation.AccountsOfUserIntoHistoryListImpl;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AccountsOfUserIntoHistoryListImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountsOfUserIntoHistoryListImpl accountsOfUserIntoHistoryList;
    @Test
    public void getStoredHistoryByClientTest(){
        //arrange
        Client clientOne = new Client("TestClient", false, "password");
        Client clientTwo = new Client("TestClient", false, "password");
        entityManager.persist(clientOne);
        entityManager.persist(clientTwo);

        Account accountOne = new Account("testNumber1", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber2", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);

        entityManager.persist(accountOne);
        entityManager.persist(accountTwo);

        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);

        //act
        ResponseEntity<ArrayList<History>> response = accountsOfUserIntoHistoryList.getStoredHistoryByClient(clientOne);
        //assert
        assertThat(response).isNotNull();
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        History body = response.getBody().get(0);
        assertEquals(body.typeOf(), "Sending to: ");
        assertEquals(body.contributorAccountNumber(), accountTwo.getAccountNumber());
        assertEquals(body.contributorName(), accountTwo.getClient().getClientName());
        assertEquals(body.amount(), "-"+transaction.getAmount()+" zł");
        assertEquals(body.dateOfTransaction(), transaction.getTimestamp());
        assertEquals(body.transactionID(), transaction.getTransactionID());
    }

    @Test
    public void getStoredHistoryByClientErrorTest(){
        //arrange
        Client clientOne = new Client("TestClient", false, "password");
        Client clientTwo = new Client("TestClient", false, "password");
        entityManager.persist(clientOne);
        entityManager.persist(clientTwo);

        Account accountTwo = new Account("testNumber2", Account.AccountType.CHECKING, clientTwo);

        entityManager.persist(accountTwo);
        //act
        ResponseEntity<ArrayList<History>> response = accountsOfUserIntoHistoryList.getStoredHistoryByClient(clientOne);
        //assert
        assertThat(response).isNotNull();
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(response.getHeaders().get("ErrorMessage").toString(), "[Client don't have any active accounts!]");

    }


}