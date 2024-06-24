package com.dmt.bankingapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class EndpointAccess {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Config which endpoints will be accessed by lambda expression
        httpSecurity.authorizeHttpRequests(configure -> configure
                // mask:
                .requestMatchers("/","/welcome").permitAll()
                .requestMatchers("/signup").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/hello").authenticated()
                .requestMatchers("/homepage").authenticated()
                // .requestMatchers(HttpMethod.<REST API METHOD>,<URI as String>).<attribute>
                .requestMatchers(HttpMethod.GET, "/test/**").authenticated()
                //security for ClientController
                .requestMatchers(HttpMethod.POST, "/client/add").permitAll()
                .requestMatchers(HttpMethod.POST, "/client/editName").authenticated()
                .requestMatchers(HttpMethod.POST, "/client/editPassword").authenticated()
                .requestMatchers(HttpMethod.POST, "/client/editAdmin/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/client/all").authenticated()
                .requestMatchers(HttpMethod.GET, "/client/byClientID").authenticated()
                //security for AccountController
                .requestMatchers(HttpMethod.POST, "/account/add").authenticated()
                .requestMatchers(HttpMethod.GET, "/account/all").authenticated()
                .requestMatchers(HttpMethod.GET, "/account/byAccountNumber").authenticated()
                //security for TransactionController
                .requestMatchers(HttpMethod.POST, "/transaction/add").authenticated()
                .requestMatchers(HttpMethod.GET, "/transaction/outgoingTransactions").authenticated()
                .requestMatchers(HttpMethod.GET, "/transaction/incomingTransactions").authenticated()
                .requestMatchers(HttpMethod.GET, "/transaction/everyTransaction").authenticated()
                .requestMatchers(HttpMethod.GET, "/transaction/byAccountNumber").authenticated()
                //security for LoanController
                .requestMatchers(HttpMethod.POST, "/loan/add").authenticated()
                .requestMatchers(HttpMethod.GET, "/loan/all").authenticated()
                .requestMatchers(HttpMethod.GET, "/loan/byLoanId").authenticated()
                //security for HistoryController
                .requestMatchers(HttpMethod.GET, "/test/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/history/**").authenticated()

        )
                //default login form
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateClient")
                                .permitAll());
        // Set http login as Basic Auth
        httpSecurity.httpBasic(Customizer.withDefaults());

        // Disable cross site request forgery token - more vulnerable
        httpSecurity.csrf(csrf -> csrf.disable());


        return httpSecurity.build();
    }
}
