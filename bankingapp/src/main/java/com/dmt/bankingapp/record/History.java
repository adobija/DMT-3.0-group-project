package com.dmt.bankingapp.record;

import java.time.LocalDateTime;

public record History(
        String typeOf,
        String contributorName,
        String contributorAccountNumber,
        String amount,
        LocalDateTime dateOfTransaction,
        Integer transactionID

) {

}
