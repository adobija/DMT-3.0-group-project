package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Loan;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
@Transactional
public class TransactionTests {

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

        Account giverAccount = new Account(giverAccountNumber, AccountType.DEPOSIT, client1);
        Account receiverAccount = new Account(receiverAccountNumber, AccountType.DEPOSIT, client2);
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

        Account giverAccount = new Account(giverAccountNumber, AccountType.DEPOSIT, client1);
        Account receiverAccount = new Account(receiverAccountNumber, AccountType.DEPOSIT, client2);
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
    
    @Test
    public void testTransactionGreaterThanLoan() {
        // Arrange
        Client loanTaker = new Client("loanTaker", false, "09876");
        Client bankClient = new Client("bankSTERS", false, "12345");

        entityManager.persist(loanTaker);
        entityManager.persist(bankClient);

        Account loanAccount = new Account("loanAccNum", AccountType.LOAN, loanTaker);
        Account checkingAccount = new Account("checkingAccNum", AccountType.CHECKING, loanTaker);
        Account bankAccount = new Account("bankAccNum", AccountType.BANK, bankClient);
        
        double checkingAccountBalance = 999999.99;
        checkingAccount.setAccountBalance(checkingAccountBalance, false);
        double principalAmount = 20000.0;
        double interestRate = 3.8;
        double commisionRate = 5.0;
        int loanDuration = 48;

        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        // Act - loan
        Loan testLoan = new Loan(loanAccount, checkingAccount, principalAmount, interestRate, commisionRate, loanDuration, bankAccount);
        entityManager.persist(testLoan);

        loanAccount.setLoan(testLoan);
        entityManager.persist(loanAccount);

        Loan foundLoan = entityManager.find(Loan.class, testLoan.getLoanID());
        double foundLoanTotalAmount = foundLoan.getTotalLoanAmount();
        double overpay = 999.99;
        double initialTransferAmount = foundLoanTotalAmount + overpay;

        // Act - transaction
        Transaction testTransaction = new Transaction(checkingAccount, loanAccount, initialTransferAmount);
        entityManager.persist(testTransaction);

        Transaction foundTransaction = entityManager.find(Transaction.class, testTransaction.getTransactionID());
        double foundTransferAmount = foundTransaction.getAmount();

        // Assert
        assertThat(foundTransaction).isNotNull();
        assertNotEquals(initialTransferAmount, foundTransferAmount);
        assertEquals(initialTransferAmount, foundTransferAmount + overpay, 0.01);
        assertEquals(checkingAccountBalance - foundTransferAmount, checkingAccountBalance - initialTransferAmount + overpay, 0.01);
    }
}
