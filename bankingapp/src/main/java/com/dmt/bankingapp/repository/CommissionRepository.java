package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    Commission findByCommissionOf(String commissionOf);
}
