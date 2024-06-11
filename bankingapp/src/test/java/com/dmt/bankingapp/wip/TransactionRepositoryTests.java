package com.dmt.bankingapp.wip;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

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

//    @Test
//    @Transactional
//    public void accountRepositoryTestUpdate(){
//        //arrange
//        Client client = new Client("TestClient",false, "password");
//        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
//        entityManager.persist(account);
//        Client newClient = new Client("NewClient", false, "haslo");
//        //act
//        account.setClient(newClient);
//        accountRepository.save(account);
//        //assert
//        assertThat(entityManager.find(Account.class, account.getAccountID()).getClient()).isEqualTo(newClient);
//    }
//
//    @Test
//    @Transactional
//    public void accountRepositoryTestDelete(){
//        //arrange
//        Client client = new Client("TestClient",false, "password");
//        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
//        entityManager.persist(account);
//        //act
//        accountRepository.delete(account);
//        //assert
//        assertThat(entityManager.find(Account.class, account.getAccountID())).isNull();
//    }
//
//    @Test
//    @Transactional
//    public void accountRepositoryTestFindById(){
//        //arrange
//        Client client = new Client("TestClient",false, "password");
//        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
//        entityManager.persist(account);
//        //act
//        Optional<Account> foundAccount = accountRepository.findById(account.getAccountID());
//        //assert
//        assertThat(foundAccount)
//                .isNotNull()
//                .contains(account);
//        assertEquals(account.getClient(), foundAccount.get().getClient());
//    }



}
