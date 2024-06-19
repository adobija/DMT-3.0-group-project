package com.dmt.bankingapp.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CalculateWithdrawDate {
    public static int monthsOfDepositIsActive(LocalDateTime creationTime, LocalDateTime withdrawDate){
        int monthsBetween = (int) ChronoUnit.MONTHS.between(creationTime, withdrawDate);
        return monthsBetween;
    }
}
