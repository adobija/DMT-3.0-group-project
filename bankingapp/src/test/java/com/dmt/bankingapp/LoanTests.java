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
        // Arrange - instantiating a user and a bank, instantiating checking and loan account for the user, and account for the bank
        String loanTaker = "userTaker";
        String bankName = "bank";

        User user1 = new User(loanTaker, false, "abc123");
        User bank = new User(bankName, false, "1234");

        String checkingAccNum = "33311100";
        String loanAccNum = "44422211";
        String bankAccNum = "00000000";

        Account checkingAccTest = new Account(checkingAccNum, "checking", user1);
        Account loanAccTest = new Account(loanAccNum, "loan", user1);
        Account bankAccTest = new Account(bankAccNum, "bank", bank);


        double checkingInitialBalace = 255.90;
        double bankInitialBalance = 999999.99;
        checkingAccTest.setAccountBalance(checkingInitialBalace, false);
        bankAccTest.setAccountBalance(bankInitialBalance, false);

        entityManager.persist(user1);
        entityManager.persist(bank);
        entityManager.persist(checkingAccTest);
        entityManager.persist(loanAccTest);
        entityManager.persist(bankAccTest);

        // Act - arranging new loan and variables required to grant the loan
        double testLoanAmount = 10000.0;
        double testIntrestRate = 5.7;
        int testLoanDuration = 60;
        Loan testLoan = new Loan(loanAccTest, checkingAccTest, testLoanAmount, testIntrestRate, testLoanDuration, bankAccTest);
        testLoan.grantLoan(testLoan.getLoanAccount(), testLoan.getCheckingAccount(), testLoan.getPrincipalLoanAmount(), testLoan.getIntrestRate(), testLoan.getLoanDuration(), testLoan.getBankAccount());
        entityManager.persist(testLoan);

        Account foundLoanAccount = entityManager.find(Account.class, loanAccTest.getAccountID());
        Account foundCheckingAccount = entityManager.find(Account.class, checkingAccTest.getAccountID());
        Account foundBankAccount = entityManager.find(Account.class, bankAccTest.getAccountID());

        // Assert - checking whether loan is launched and money correctly transfered to checking account and bank account
        assertThat(foundCheckingAccount).isNotNull();
        assertThat(foundLoanAccount).isNotNull();
        assertThat(foundBankAccount).isNotNull();

        double testCheckingAccBalance = checkingInitialBalace + testLoanAmount;
        assertEquals(testCheckingAccBalance, foundCheckingAccount.getAccountBalance());

        double testLoanAccBalance = -testLoanAmount - testLoan.intrestAmount(testLoanAmount, testIntrestRate, testLoanDuration);
        assertEquals(testLoanAccBalance, foundLoanAccount.getAccountBalance());

        double testBankAccBalance = bankInitialBalance + testLoan.intrestAmount(testLoanAmount, testIntrestRate, testLoanDuration);
        assertEquals(testBankAccBalance, foundBankAccount.getAccountBalance());
    }
}
