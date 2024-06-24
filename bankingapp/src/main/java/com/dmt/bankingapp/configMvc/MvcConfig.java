package com.dmt.bankingapp.configMvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/").setViewName("welcome");
		registry.addViewController("/welcome").setViewName("welcome");
		registry.addViewController("/signup").setViewName("signup");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/homepage").setViewName("homepage");

		registry.addViewController("/hello").setViewName("hello");

		registry.addViewController("/admin").setViewName("admin");
		


	}

}