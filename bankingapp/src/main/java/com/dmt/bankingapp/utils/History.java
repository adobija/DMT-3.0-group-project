package com.dmt.bankingapp.utils;

public record History(
        String contributorName,
        String contributorAccountNumber,
        String amount,
        String dateOfTransaction,
        Double balanceAfterTransaction,
        Integer transactionID
) {

}
