package com.dmt.bankingapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalPlacesAdjuster {

    public static double adjustToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP);
        bd = bd.setScale(2, RoundingMode.DOWN);
        return bd.doubleValue();
    }  
}
