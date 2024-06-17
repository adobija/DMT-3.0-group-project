package com.dmt.bankingapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPlacesAdjuster {

    public static double adjustToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
