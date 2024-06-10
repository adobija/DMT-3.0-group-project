package com.dmt.bankingapp.repository;

import com.dmt.bankingapp.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Account findByClientId(Integer clientID);
}
