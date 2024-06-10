package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Integer> {
//    Client findByClientName(String clientName);
}
