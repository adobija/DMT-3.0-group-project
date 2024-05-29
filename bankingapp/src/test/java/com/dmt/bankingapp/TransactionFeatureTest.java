package com.dmt.bankingapp;


import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.User;
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
public class TransactionFeatureTest {
    //arrange
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testForTransactions() {
        //Arrange
        String giverClient = "userFirst";
        String receiverClient = "userSecond";

        User user1 = new User(giverClient, false, "test123");
        User user2 = new User(receiverClient, false, "SuperCoolTest");

        String giverAccountNumber = "222111222";
        String receiverAccountNumber = "111333999";

        Account giverAccount = new Account(giverAccountNumber, "savings", user1);
        Account receiverAccount = new Account(receiverAccountNumber, "savings", user2);
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
        String giverClient = "userFirst";
        String receiverClient = "userSecond";

        User user1 = new User(giverClient, false, "test123");
        User user2 = new User(receiverClient, false, "SuperCoolTest");

        String giverAccountNumber = "222111222";
        String receiverAccountNumber = "111333999";

        Account giverAccount = new Account(giverAccountNumber, "savings", user1);
        Account receiverAccount = new Account(receiverAccountNumber, "savings", user2);
        double amount = 500.0;
        giverAccount.setAccountBalance(amount, false);
        entityManager.persist(giverAccount);
        entityManager.persist(receiverAccount);

        double amountInTransaction = 300.0;
        Transaction transaction = new Transaction(giverAccount, receiverAccount, amountInTransaction);
        entityManager.persist(transaction);
        String now = LocalDateTime.now().toString().split(".")[0];
        //Act
        Transaction transaction1 = entityManager.find(Transaction.class, transaction.getTransactionID());

        String timestampFromTransactionRecord = transaction1.getTimestamp().toString().split(".")[0];
        //Assert
        assertEquals(now, timestampFromTransactionRecord);
    }

}
