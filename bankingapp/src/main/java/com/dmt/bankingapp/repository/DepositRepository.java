package com.dmt.bankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmt.bankingapp.entity.Deposit;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    
}
