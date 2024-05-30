package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class LoanTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testLoans() {
        // Arrange
        String loanTaker = "userTaker";

        User user1 = new User(loanTaker, false, "abc123");

        String checkingAccNum = "33311100";
        String loanAccNum = "44422211";

        Account checkingAccTest = new Account(checkingAccNum, "checking", user1);
        Account loanAccTest = new Account(loanAccNum, "loan", user1);

        double initialBalace = 255.90;
        checkingAccTest.setAccountBalance(initialBalace, false);

        entityManager.persist(user1);
        entityManager.persist(checkingAccTest);
        entityManager.persist(loanAccTest);

        // Act
        double testLoanAmount = 10000.0;
        Loan loan = new Loan(loanAccTest, checkingAccTest, testLoanAmount);
        loan.grantLoan(loan.getLoanAccount(), loan.getCheckingAccount(), loan.getPrincipleLoanAmount());
        entityManager.persist(loan);

        Account foundLoanAccount = entityManager.find(Account.class, loanAccTest.getAccountID());
        Account foundCheckingAccount = entityManager.find(Account.class, checkingAccTest.getAccountID());

        // Assert
        assertThat(foundCheckingAccount).isNotNull();
        assertThat(foundLoanAccount).isNotNull();

        assertEquals(initialBalace + testLoanAmount, foundCheckingAccount.getAccountBalance());
        assertEquals(-testLoanAmount, foundLoanAccount.getAccountBalance());
    }
}
