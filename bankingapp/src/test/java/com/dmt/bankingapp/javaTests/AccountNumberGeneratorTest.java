package com.dmt.bankingapp.javaTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dmt.bankingapp.utils.AccountNumberGenerator;

public class AccountNumberGeneratorTest {
    @Test
    public void testGenerateAccountNumber() {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();

        // Check if the account number starts with the prefix
        assertTrue(accountNumber.startsWith("2024DMT3.0_"), "Account number should start with '2024DMT3.0_'");

        // Check if the account number length is correct
        assertEquals(21, accountNumber.length(), "Account number should be 21 characters long");

         // Check if the part after the prefix contains only digits
        String numericPart = accountNumber.substring(11); // "2024DMT3.0_" is 11 characters long
        assertTrue(numericPart.matches("\\d{10}"), "The part after the prefix should be exactly 10 digits");
    }
}
