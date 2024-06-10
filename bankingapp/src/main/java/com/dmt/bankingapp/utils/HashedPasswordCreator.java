package com.dmt.bankingapp.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashedPasswordCreator {
    public static String createBcryptHashedPassword(String plainTextPassword) {
        int numberOfRounds = 10;
        String hashingSalt = BCrypt.gensalt(numberOfRounds);
        return "{bcrypt}" + BCrypt.hashpw(plainTextPassword, hashingSalt);
    }
}
