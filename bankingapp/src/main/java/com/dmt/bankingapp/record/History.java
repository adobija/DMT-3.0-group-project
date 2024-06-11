package com.dmt.bankingapp.record;

import java.time.LocalDateTime;

public record History(
        String contributorName,
        String contributorAccountNumber,
        String amount,
        LocalDateTime dateOfTransaction,
        Double balanceAfterTransaction,
        Integer transactionID
) {

}
