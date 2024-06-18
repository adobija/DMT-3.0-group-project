package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Comission;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.ComissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingappApplication {

    public static void main(String[] args) {
		SpringApplication.run(BankingappApplication.class, args);
	}

	@Autowired
	private final ClientRepository clientRepository;

	@Autowired
	private final AccountRepository accountRepository;

	@Autowired
	private final ComissionRepository comissionRepository;

	public BankingappApplication(ClientRepository clientRepository, AccountRepository accountRepository, ComissionRepository comissionRepository) {
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.comissionRepository = comissionRepository;
	}

	@Bean
	public CommandLineRunner commandLineRunner (){
		return runner -> {
			Client checkIfExist = clientRepository.findByClientName("BankOwner");

			if(checkIfExist == null){
				Client bank = new Client("BankOwner", true, "dmtprojekt2024");
				Account accountForLoans = new Account("BANK_LOAN", Account.AccountType.BANK, bank);
				Account accountForDeposits = new Account("BANK_DEPOSIT", Account.AccountType.BANK, bank);
				accountForLoans.setAccountBalance(30000000, false);

				clientRepository.save(bank);
				accountRepository.save(accountForDeposits);
				accountRepository.save(accountForLoans);
			}

			Comission checkComissionForLoan = comissionRepository.findByComissionOf("LOAN");
			Comission checkComissionForDeposit = comissionRepository.findByComissionOf("DEPOSIT");

			if(checkComissionForLoan == null){
				Comission comissionForLoan = new Comission(10, "LOAN");
				comissionRepository.save(comissionForLoan);
			}
			if(checkComissionForDeposit == null){
				Comission comissionForDeposit = new Comission(10, "DEPOSIT");
				comissionRepository.save(comissionForDeposit);
			}


		};
	}
}