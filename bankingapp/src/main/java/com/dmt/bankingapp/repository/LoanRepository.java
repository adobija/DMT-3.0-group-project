package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    Loan findByLoanID (int loanId);
}
