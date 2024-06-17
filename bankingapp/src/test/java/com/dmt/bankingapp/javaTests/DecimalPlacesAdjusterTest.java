package com.dmt.bankingapp.javaTests;

import org.junit.jupiter.api.Test;

import com.dmt.bankingapp.utils.DecimalPlacesAdjuster;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecimalPlacesAdjusterTest {

    @Test
    public void testAdjustToTwoDecimalPlaces() {
        assertEquals(123.46, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(123.4567), 0.0001);
        assertEquals(123.46, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(123.456), 0.0001);
        assertEquals(0.00, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(0.004), 0.0001);
        assertEquals(0.01, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(0.014), 0.0001);
        assertEquals(-123.46, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(-123.4567), 0.0001);
        assertEquals(-123.46, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(-123.456), 0.0001);
        assertEquals(1.00, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(1.0009), 0.0001);
        assertEquals(1.01, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(1.014), 0.0001);
    }
}
