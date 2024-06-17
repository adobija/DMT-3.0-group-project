package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingappApplication {

    public BankingappApplication(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(BankingappApplication.class, args);
	}

	@Autowired
	private final ClientRepository clientRepository;

	@Autowired
	private final AccountRepository accountRepository;

	@Bean
	public CommandLineRunner commandLineRunner (){
		return runner -> {
			Client checkIfExist = clientRepository.findByClientName("BankOwner");

			if(checkIfExist == null){
				Client bank = new Client("BankOwner", true, "dmtprojekt2024");
				Account accountForLoans = new Account("BANK_LOAN", Account.AccountType.LOAN, bank);
				Account accountForDeposits = new Account("BANK_DEPOSIT", Account.AccountType.DEPOSIT, bank);
				accountForLoans.setAccountBalance(30000000, false);

				clientRepository.save(bank);
				accountRepository.save(accountForDeposits);
				accountRepository.save(accountForLoans);
			}
		};
	}

}