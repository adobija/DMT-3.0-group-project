package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Transaction;
import com.dmt.bankingapp.entity.User;
import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Account;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
public class LoanTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testLoans() {
        //Arrange
        String loanTaker = "userTaker";

        User user1 = new User(loanTaker, false, "abc123");
        
        String checkingAccountNum = "33311100";
        String loanAccountNum = "44422211";

        Account loanCheckingAcc = new Account(checkingAccountNum, "checking", user1);
        Account loanLoanAcc = new Account(loanAccountNum, "loan", user1);

        entityManager.persist(loanCheckingAcc);
        entityManager.persist(loanLoanAcc);

        //Act
        double testLoanAmount = 10000.0;
        Loan loan = new Loan(loanCheckingAcc, loanLoanAcc, testLoanAmount);
        loan.grantLoan(loan.getLoanAccount(), loan.getCheckingAccount(), loan.getLoanAmount());
        entityManager.persist(loan);

        Account foundCheckingAccount = entityManager.find(Account.class, loanCheckingAcc.getAccountID());
        Account foundLoanAccount = entityManager.find(Account.class, loanLoanAcc.getAccountID());
        
        //Assert
        assertThat(foundCheckingAccount).isNotNull();
        assertThat(foundLoanAccount).isNotNull();

        assertEquals(testLoanAmount, foundCheckingAccount.getAccountBalance());
        assertEquals(0 - testLoanAmount, foundLoanAccount.getAccountBalance());


    }
}
