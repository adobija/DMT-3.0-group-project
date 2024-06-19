package com.dmt.bankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dmt.bankingapp.entity.Commission;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    Commission findByCommissionOf(String commissionOf);
}
