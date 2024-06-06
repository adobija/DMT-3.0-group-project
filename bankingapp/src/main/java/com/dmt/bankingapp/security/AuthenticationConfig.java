package com.dmt.bankingapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class AuthenticationConfig {

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        // New jdbc user management service instance
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // Query to fetch users login and password by login
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "SELECT client_name,bcrypt_client_password, true FROM clients where client_name=?");
        // Query to fetch empty list of authorities, it is required but we don't use it
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT client_name, is_admin from clients where client_name=?");

        return jdbcUserDetailsManager;
    }
}
