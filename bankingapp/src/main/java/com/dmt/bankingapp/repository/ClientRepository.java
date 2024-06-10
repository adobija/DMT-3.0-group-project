package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
//    Client findByClientName(String clientName);
}
