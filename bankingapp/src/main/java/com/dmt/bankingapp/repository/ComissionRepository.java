package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Comission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComissionRepository extends JpaRepository<Comission, Integer> {
    Comission findByComissionOf(String comissionOf);
}
