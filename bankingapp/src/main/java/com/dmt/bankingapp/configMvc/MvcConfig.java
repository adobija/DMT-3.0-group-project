package com.dmt.bankingapp.configMvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		// index site
		registry.addViewController("/").setViewName("indexTemplates/welcome");
		registry.addViewController("/homepage").setViewName("indexTemplates/hello");
		//register
		registry.addViewController("/signup").setViewName("registerTemplates/signup");
		// login
		registry.addViewController("/login").setViewName("loginTemplates/login");
		// other
//		registry.addViewController("/hello").setViewName("indexTemplates/hello");
		registry.addViewController("/admin").setViewName("admin");
    	registry.addViewController("/about").setViewName("other/about");
		//commission
		registry.addViewController("/setForLoanCommission").setViewName("commissionTemplates/loanCommission");
		registry.addViewController("/setForDeposit").setViewName("commissionTemplates/depositCommission");
		registry.addViewController("/setForLoanInterest").setViewName("commissionTemplates/loanInterestCommission");
		//client
		registry.addViewController("/changeName").setViewName("clientTemplates/editNameForm");
		registry.addViewController("/changePassword").setViewName("clientTemplates/editPasswordForm");
		registry.addViewController("/changeAdminPermission").setViewName("clientTemplates/editAdminForm");
		registry.addViewController("/byClientIdForm").setViewName("clientTemplates/byClientIdForm");
		//admin
		registry.addViewController("/admin").setViewName("adminTemplates/adminPanel");
		//transaction
//		registry.addViewController("/transaction/add").setViewName("transactionTemplates/add");
//		registry.addViewController("/outgoingTransactions").setViewName("transactionTemplates/outgoing");
//		registry.addViewController("/incomingTransactions").setViewName("transactionTemplates/incoming");
		registry.addViewController("/transaction/getAll").setViewName("transactionTemplates/getAll");
		registry.addViewController("/transaction/accNumber").setViewName("transactionTemplates/accNumberForm");
		//loan
		registry.addViewController("/takeLoan").setViewName("loanTemplates/addForm");
		registry.addViewController("/loan/find").setViewName("loanTemplates/findloan");
		//installment
		registry.addViewController("/myAll").setViewName("installmentTemplates/myAll");
		registry.addViewController("/installments/given").setViewName("installmentTemplates/givenForm");
		registry.addViewController("/installments/loanId").setViewName("installmentTemplates/loanInstallmentForm");
		//		registry.addViewController("/next").setViewName("installmentTemplates/next");
		registry.addViewController("/given").setViewName("installmentTemplates/given");
		registry.addViewController("/loan").setViewName("installmentTemplates/loan");
		registry.addViewController("/all").setViewName("installmentTemplates/all");
		//deposit
		registry.addViewController("/addDeposit").setViewName("depositTemplates/addDepositForm");
//		registry.addViewController("/withdrawDeposit").setViewName("depositTemplates/withdraw");




	}
}