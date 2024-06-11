package com.dmt.bankingapp.utils;

import java.util.Random;

public class AccountNumberGenerator {

    public static String generateAccountNumber() {

        Random randomNumber = new Random();
        String accountNumber = "2024DMT3.0_";

        for (int i = 0; i < 10; i++) {
            int generatedNumber = randomNumber.nextInt(10);
            accountNumber += Integer.toString(generatedNumber);
        }        
        return accountNumber;
    }
    
}
