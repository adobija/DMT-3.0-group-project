package com.dmt.bankingapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPlacesAdjuster {

    public static double adjustToTwoDecimalPlaces(double value) {
        // Restricting amounts less than 0.01 for positive values
        if (value < 0.01 && value > 0) {
            return 0.00;
        }
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
