package com.dmt.bankingapp.springTests.repositoryTests;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void accountRepositoryTestInsertion(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
        //act
        Account savedAccount = accountRepository.save(account);
        //assert
        assertThat(entityManager.find(Account.class, savedAccount.getAccountID())).isEqualTo(account);
    }

    @Test
    @Transactional
    public void accountRepositoryTestUpdate(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
        entityManager.persist(account);
        Client newClient = new Client("NewClient", false, "haslo");
        //act
        account.setClient(newClient);
        accountRepository.save(account);
        //assert
        assertThat(entityManager.find(Account.class, account.getAccountID()).getClient()).isEqualTo(newClient);
    }

    @Test
    @Transactional
    public void accountRepositoryTestDelete(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
        entityManager.persist(account);
        //act
        accountRepository.delete(account);
        //assert
        assertThat(entityManager.find(Account.class, account.getAccountID())).isNull();
    }

    @Test
    @Transactional
    public void accountRepositoryTestFindById(){
        //arrange
        Client client = new Client("TestClient",false, "password");
        Account account = new Account("testNumber", Account.AccountType.CHECKING, client);
        entityManager.persist(account);
        //act
        Optional<Account> foundAccount = accountRepository.findById(account.getAccountID());
        //assert
        assertThat(foundAccount)
                .isNotNull()
                .contains(account);
        assertEquals(account.getClient(), foundAccount.get().getClient());
    }



}
