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
                // .requestMatchers(HttpMethod.<REST API METHOD>,<URI as String>).<attribute>
                .requestMatchers(HttpMethod.GET, "/test/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/history/**").authenticated()
        );
        // Set http login as Basic Auth
        httpSecurity.httpBasic(Customizer.withDefaults());

        // Disable cross site request forgery token - more vulnerable
        // httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }
}
