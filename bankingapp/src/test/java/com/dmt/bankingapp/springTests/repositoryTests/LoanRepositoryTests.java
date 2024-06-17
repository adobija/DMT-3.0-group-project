package com.dmt.bankingapp.springTests.repositoryTests;

import com.dmt.bankingapp.entity.Loan;
import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LoanRepositoryTests {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    public void loanRepositoryTestInsertion() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account loanAccount = new Account("loanAccountNumber", Account.AccountType.CHECKING, client);
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccountNumber", Account.AccountType.DEPOSIT, client);

        entityManager.persist(client);
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        Loan loan = new Loan(loanAccount, checkingAccount, 10000.00, 5.0, 1.0, 12, bankAccount);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setTotalLoanAmout(10500.00);
        loan.setLeftToPay(10500.00);
        loan.setIsActive(true);

        // act
        Loan savedLoan = loanRepository.save(loan);

        // assert
        assertThat(entityManager.find(Loan.class, savedLoan.getLoanID())).isEqualTo(loan);
    }

    @Test
    @Transactional
    public void loanRepositoryTestUpdate() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account loanAccount = new Account("loanAccountNumber", Account.AccountType.CHECKING, client);
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccountNumber", Account.AccountType.DEPOSIT, client);

        entityManager.persist(client);
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        Loan loan = new Loan(loanAccount, checkingAccount, 10000.00, 5.0, 1.0, 12, bankAccount);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setTotalLoanAmout(10500.00);
        loan.setLeftToPay(10500.00);
        loan.setIsActive(true);

        entityManager.persist(loan);

        double newPrincipalAmount = 12000.00;

        // act
        loan.setPrincipalLoanAmout(newPrincipalAmount);
        loanRepository.save(loan);

        // assert
        assertThat(entityManager.find(Loan.class, loan.getLoanID()).getPrincipalLoanAmount()).isEqualTo(newPrincipalAmount);
    }

    @Test
    @Transactional
    public void loanRepositoryTestDelete() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account loanAccount = new Account("loanAccountNumber", Account.AccountType.CHECKING, client);
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccountNumber", Account.AccountType.DEPOSIT, client);

        entityManager.persist(client);
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        Loan loan = new Loan(loanAccount, checkingAccount, 10000.00, 5.0, 1.0, 12, bankAccount);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setTotalLoanAmout(10500.00);
        loan.setLeftToPay(10500.00);
        loan.setIsActive(true);

        entityManager.persist(loan);

        // act
        loanRepository.delete(loan);

        // assert
        assertThat(entityManager.find(Loan.class, loan.getLoanID())).isNull();
    }

    @Test
    @Transactional
    public void loanRepositoryTestFindById() {
        // arrange
        Client client = new Client("TestClient", false, "password");
        Account loanAccount = new Account("loanAccountNumber", Account.AccountType.CHECKING, client);
        Account checkingAccount = new Account("checkingAccountNumber", Account.AccountType.CHECKING, client);
        Account bankAccount = new Account("bankAccountNumber", Account.AccountType.DEPOSIT, client);

        entityManager.persist(client);
        entityManager.persist(loanAccount);
        entityManager.persist(checkingAccount);
        entityManager.persist(bankAccount);

        Loan loan = new Loan(loanAccount, checkingAccount, 10000.00, 5.0, 1.0, 12, bankAccount);
        loan.setDateOfLoan(LocalDateTime.now());
        loan.setTotalLoanAmout(10500.00);
        loan.setLeftToPay(10500.00);
        loan.setIsActive(true);

        entityManager.persist(loan);

        // act
        Optional<Loan> foundLoan = loanRepository.findById(loan.getLoanID());

        // assert
        assertThat(foundLoan)
                .isNotNull()
                .contains(loan);
        assertEquals(loan.getClient(), foundLoan.get().getClient());
    }
}