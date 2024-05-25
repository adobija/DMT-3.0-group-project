package com.dmt.bankingapp;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class PasswordLoader {

    @PostConstruct
    public void init() throws IOException {
        String passwordFilePath = "password/db-password.txt";
        String password = new String(Files.readAllBytes(Paths.get(passwordFilePath))).trim();
        System.setProperty("DB_PASSWORD", password);
    }
}
