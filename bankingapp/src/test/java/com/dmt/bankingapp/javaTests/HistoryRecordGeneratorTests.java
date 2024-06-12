package com.dmt.bankingapp.javaTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.record.History;
import com.dmt.bankingapp.utils.HistoryRecordGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@Transactional
public class HistoryRecordGeneratorTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    void castDataToCreateHistoryRecordTestExpense() {
        //arrange
        Client clientGiver = new Client("giverTest",false,"testPassowrd");
        Client clientReceiver = new Client("receiverTest",false,"testPassowrd");

        Account accountGiver = new Account("testAccountNumberGiver", Account.AccountType.CHECKING, clientGiver);
        Account accountReceiver = new Account("testAccountNumberReceiver", Account.AccountType.CHECKING, clientReceiver);
        accountGiver.setAccountBalance(500,false);

        Transaction transaction = new Transaction(accountGiver, accountReceiver, 210.0);

        entityManager.persist(transaction);
        //act
        History history = HistoryRecordGenerator.castDataToCreateHistoryRecord(transaction,accountGiver);
        //assert
        assertEquals("Sending to: ",history.typeOf());
        assertEquals(accountReceiver.getClient().getClientName(), history.contributorName());
        assertEquals(accountReceiver.getAccountNumber(), history.contributorAccountNumber());
        assertEquals("-210.0 zł", history.amount());
        assertEquals(transaction.getTimestamp(), history.dateOfTransaction());
        assertEquals(transaction.getTransactionID(), history.transactionID());
    }

    @Test
    @Transactional
    void castDataToCreateHistoryRecordTestIncome() {
        //arrange
        Client clientGiver = new Client("giverTest",false,"testPassowrd");
        Client clientReceiver = new Client("receiverTest",false,"testPassowrd");

        Account accountGiver = new Account("testAccountNumberGiver", Account.AccountType.CHECKING, clientGiver);
        Account accountReceiver = new Account("testAccountNumberReceiver", Account.AccountType.CHECKING, clientReceiver);
        accountGiver.setAccountBalance(500,false);

        Transaction transaction = new Transaction(accountGiver, accountReceiver, 210.0);

        entityManager.persist(transaction);
        //act
        History history = HistoryRecordGenerator.castDataToCreateHistoryRecord(transaction,accountReceiver);
        //assert
        assertEquals("Receiving from: ",history.typeOf());
        assertEquals(accountGiver.getClient().getClientName(), history.contributorName());
        assertEquals(accountGiver.getAccountNumber(), history.contributorAccountNumber());
        assertEquals("+210.0 zł", history.amount());
        assertEquals(transaction.getTimestamp(), history.dateOfTransaction());
        assertEquals(transaction.getTransactionID(), history.transactionID());
    }
}