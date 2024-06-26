package com.dmt.bankingapp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAdjuster {
    public static String getDate(LocalDateTime inputDate) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = inputDate.format(dateFormatter);

        return formattedDate;
    }
}
