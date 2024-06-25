package com.dmt.bankingapp.configMvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		//index site
		registry.addViewController("/").setViewName("indexTemplates/welcome");
		registry.addViewController("/homepage").setViewName("indexTemplates/homepage");
		//register
		registry.addViewController("/signup").setViewName("registerTemplates/signup");
		//login
		registry.addViewController("/login").setViewName("loginTemplates/login");
		//other
		registry.addViewController("/hello").setViewName("indexTemplates/hello");
		registry.addViewController("/admin").setViewName("admin");

		registry.addViewController("/findClientById").setViewName("testTemplates/testForm");

		//commission
		registry.addViewController("/setForLoanCommission").setViewName("commissionTemplates/loanCommission");
		registry.addViewController("/setForDeposit").setViewName("commissionTemplates/depositCommission");
		registry.addViewController("/setForLoanInterest").setViewName("commissionTemplates/loanInterestCommission");



	}

}