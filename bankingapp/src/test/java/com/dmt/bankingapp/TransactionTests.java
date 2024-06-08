package com.dmt.bankingapp;


import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class TransactionTests {
    //arrange
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testForTransactions() {
        //Arrange
        String giverClient = "clientFirst";
        String receiverClient = "clientSecond";

        Client client1 = new Client(giverClient, false, "test123");
        Client client2 = new Client(receiverClient, false, "SuperCoolTest");

        String giverAccountNumber = "222111222";
        String receiverAccountNumber = "111333999";

        Account giverAccount = new Account(giverAccountNumber, "savings", client1);
        Account receiverAccount = new Account(receiverAccountNumber, "savings", client2);
        double amount = 500.0;
        giverAccount.setAccountBalance(amount, false);
        entityManager.persist(giverAccount);
        entityManager.persist(receiverAccount);

        //Act
        double amountInTransaction = 300.0;
        Transaction transaction = new Transaction(giverAccount, receiverAccount, amountInTransaction);
        entityManager.persist(transaction);

        Account foundGiverAccount = entityManager.find(Account.class, giverAccount.getAccountID());
        Account foundReceiverAccount = entityManager.find(Account.class, receiverAccount.getAccountID());
        //Assert
        assertThat(foundGiverAccount).isNotNull();
        assertThat(foundReceiverAccount).isNotNull();

        assertEquals(amount-amountInTransaction, foundGiverAccount.getAccountBalance());
        assertEquals(amountInTransaction, foundReceiverAccount.getAccountBalance());
    }

    @Test
    public void testForDateAndTime(){
        //Arrange
        String giverClient = "clientFirst";
        String receiverClient = "clientSecond";

        Client client1 = new Client(giverClient, false, "test123");
        Client client2 = new Client(receiverClient, false, "SuperCoolTest");

        String giverAccountNumber = "222111222";
        String receiverAccountNumber = "111333999";

        Account giverAccount = new Account(giverAccountNumber, "savings", client1);
        Account receiverAccount = new Account(receiverAccountNumber, "savings", client2);
        double amount = 500.0;
        giverAccount.setAccountBalance(amount, false);
        entityManager.persist(giverAccount);
        entityManager.persist(receiverAccount);

        double amountInTransaction = 300.0;
        Transaction transaction = new Transaction(giverAccount, receiverAccount, amountInTransaction);
        entityManager.persist(transaction);
        String now = LocalDateTime.now().toString().split("\\.")[0];

        //Act
        Transaction transaction1 = entityManager.find(Transaction.class, transaction.getTransactionID());

        //LocalDateTime look like YYYY-MM-DD{timezone}HH:MM:SS.{9 decimal points for milisecond}
        //so I am comparing whole string before coma
        String timestampFromTransactionRecord = transaction1.getTimestamp().toString().split("\\.")[0];

        //Assert
        assertEquals(now, timestampFromTransactionRecord);
    }

}
