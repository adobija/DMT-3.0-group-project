package com.dmt.bankingapp;

import com.dmt.bankingapp.entity.Account;
import com.dmt.bankingapp.entity.Client;
import com.dmt.bankingapp.entity.Commission;
import com.dmt.bankingapp.repository.AccountRepository;
import com.dmt.bankingapp.repository.ClientRepository;
import com.dmt.bankingapp.repository.CommissionRepository;
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
	private final CommissionRepository commissionRepository;

	public BankingappApplication(ClientRepository clientRepository, AccountRepository accountRepository, CommissionRepository commissionRepository) {
		this.clientRepository = clientRepository;
		this.accountRepository = accountRepository;
		this.commissionRepository = commissionRepository;
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

			Commission checkCommissionForLoan = commissionRepository.findByCommissionOf("LOAN");
			Commission checkCommissionForDeposit = commissionRepository.findByCommissionOf("DEPOSIT");

			if(checkCommissionForLoan == null){
				Commission commissionForLoan = new Commission(10, "LOAN");
				commissionRepository.save(commissionForLoan);
			}
			if(checkCommissionForDeposit == null){
				Commission commissionForDeposit = new Commission(10, "DEPOSIT");
				commissionRepository.save(commissionForDeposit);
			}


		};
	}
}