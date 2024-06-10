package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByClientID(int clientID);
}
