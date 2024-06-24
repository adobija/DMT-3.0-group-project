package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmt.bankingapp.entity.Deposit;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Integer> {
    List<Deposit> getAllByClient (Client client);
}
