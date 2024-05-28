package com.dmt.bankingapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
public class BankingappApplicationTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testCreateAccount() {
		// Arrange
        User user = new User("testUser", false, "password123");
        entityManager.persist(user);

        Account account = new Account("123456789", 1000.0, "savings");
        account.setUser(user);
        entityManager.persist(account);

		// Act
        Account foundAccount = entityManager.find(Account.class, account.getAccountID());
        User foundUser = entityManager.find(User.class, user.getUserID());

		// Assert
        assertThat(foundAccount).isNotNull();
        assertThat(foundUser).isNotNull();
    }
}
