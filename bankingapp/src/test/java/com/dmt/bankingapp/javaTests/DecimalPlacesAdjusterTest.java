package com.dmt.bankingapp.javaTests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.dmt.bankingapp.utils.DecimalPlacesAdjuster;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecimalPlacesAdjusterTest {

    @ParameterizedTest
    @CsvSource({
        "123.4567, 123.46",
        "123.456, 123.46",
        "0.004, 0.00",
        "0.014, 0.01",
        "-123.4567, -123.46",
        "-123.456, -123.46",
        "1.0009, 1.00",
        "1.014, 1.01",
        "456.789, 456.79",
        "-0.123, -0.12",
        "9.9999, 10.00",
        "10.505, 10.51",
        "-10.505, -10.51",
        "3.14159, 3.14",
        "3.14159265358979323846, 3.14",
        "3.1415926535897932384626433832795, 3.14",
        "2.71828, 2.72",
        "0.1, 0.10",
        "0.01, 0.01",
        "0.001, 0.00",
        "0.105, 0.10",
        "0.106, 0.11",
        "999.9999, 1000.00",
        "-999.9999, -1000.00",
        "99999.9999, 100000.00"
    })
    public void testAdjustToTwoDecimalPlaces(double input, double expected) {
        assertEquals(expected, DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(input), 0.0001);
    }
}
