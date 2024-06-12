package com.dmt.bankingapp.springTests.repositoryTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TransactionRepositoryTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void transactionRepositoryTestInsertion(){
        //arrange
        Client clientOne = new Client("TestClient",false, "password");
        Client clientTwo = new Client("TestClient",false, "password");
        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        //act
        Transaction savedTransaction = transactionRepository.save(transaction);
        //assert
        assertThat(entityManager.find(Transaction.class, savedTransaction.getTransactionID())).isEqualTo(transaction);
    }

    @Test
    @Transactional
    public void transactionRepositoryTestUpdate() throws InterruptedException {
        //arrange
        Client clientOne = new Client("TestClient",false, "password");
        Client clientTwo = new Client("TestClient",false, "password");
        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);
        Thread.sleep(1500);
        LocalDateTime newTime = LocalDateTime.now();
        //act
        transaction.setTimestamp(newTime);
        transactionRepository.save(transaction);
        //assert
        assertThat(entityManager.find(Transaction.class, transaction.getTransactionID()).getTimestamp().isEqual(newTime));
    }

    @Test
    @Transactional
    public void transactionRepositoryTestDelete(){
        //arrange
        Client clientOne = new Client("TestClient",false, "password");
        Client clientTwo = new Client("TestClient",false, "password");
        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);
        //act
        transactionRepository.delete(transaction);
        //assert
        assertThat(entityManager.find(Transaction.class, transaction.getTransactionID())).isNull();
    }

    @Test
    @Transactional
    public void transactionRepositoryTestFindById(){
        //arrange
        Client clientOne = new Client("TestClient",false, "password");
        Client clientTwo = new Client("TestClient",false, "password");
        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);
        //act
        Optional<Transaction> foundTransaction = transactionRepository.findById(transaction.getTransactionID());
        //assert
        assertThat(foundTransaction)
                .isNotNull()
                .contains(transaction);
        assertEquals(transaction.getTimestamp(), foundTransaction.get().getTimestamp());
        assertEquals(transaction.getAmount(), foundTransaction.get().getAmount());
        assertEquals(transaction.getTransactionID(), foundTransaction.get().getTransactionID());
    }

    @Test
    @Transactional
    public void transactionRepositoryTestFindByGiver(){
        //arrange
        Client clientOne = new Client("Giver",false, "password");
        Client clientTwo = new Client("Receiver",false, "password");
        entityManager.persist(clientOne);
        entityManager.persist(clientTwo);

        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        entityManager.persist(accountOne);
        entityManager.persist(accountTwo);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);
        //act
        List<Transaction> foundTransaction = transactionRepository.findByGiver(accountOne);
        //assert
        assertThat(foundTransaction).isNotNull();
        assertThat(foundTransaction.isEmpty()).isFalse();
        assertEquals(foundTransaction.get(0).getGiver().getClient(), clientOne);
    }

    @Test
    @Transactional
    public void transactionRepositoryTestFindByReceiver(){
        //arrange
        Client clientOne = new Client("Giver",false, "password");
        Client clientTwo = new Client("Receiver",false, "password");
        entityManager.persist(clientOne);
        entityManager.persist(clientTwo);

        Account accountOne = new Account("testNumber", Account.AccountType.CHECKING, clientOne);
        Account accountTwo = new Account("testNumber", Account.AccountType.CHECKING, clientTwo);
        accountOne.setAccountBalance(5000.0, false);
        entityManager.persist(accountOne);
        entityManager.persist(accountTwo);
        Transaction transaction = new Transaction(accountOne, accountTwo, 20.0);
        entityManager.persist(transaction);
        //act
        List<Transaction> foundTransaction = transactionRepository.findByReceiver(accountTwo);
        //assert
        assertThat(foundTransaction).isNotNull();
        assertThat(foundTransaction.isEmpty()).isFalse();
        assertEquals(foundTransaction.get(0).getReceiver().getClient(), clientTwo);
    }


}
