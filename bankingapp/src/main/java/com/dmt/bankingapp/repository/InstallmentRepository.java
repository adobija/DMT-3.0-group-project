package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Installment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Integer> {
    Installment findByInstallmentID (int installmentId);
}
