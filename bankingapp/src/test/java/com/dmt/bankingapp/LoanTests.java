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

    @Test
    public void testLoans() {
        //Arrange
        String loanTaker = "userTaker";

        User user1 = new User(loanTaker, false, "abc123");
        
        String checkingAccountNum = "33311100";
        String loanAccountNum = "44422211";

        Account loanCheckingAcc = new Account(checkingAccountNum, "checking", user1);
        Account loanLoanAcc = new Account(loanAccountNum, "loan", user1);

        //Act
        double loanAmount = 10000.0;
        Loan loan = new Loan(loanCheckingAcc, loanLoanAcc, loanAmount);
        loan.grantLoan(loanCheckingAcc, loanLoanAcc, loanAmount);
        // entityManager.persist(loan);

        Account foundGiverAccount = entityManager.find(Account.class, loanCheckingAcc.getAccountID());

        //Assert
        assertThat(foundGiverAccount).isNotNull();
        assertThat(foundReceiverAccount).isNotNull();

        assertEquals(amount-amountInTransaction, foundGiverAccount.getAccountBalance());
        assertEquals(amountInTransaction, foundReceiverAccount.getAccountBalance());


    }
}
