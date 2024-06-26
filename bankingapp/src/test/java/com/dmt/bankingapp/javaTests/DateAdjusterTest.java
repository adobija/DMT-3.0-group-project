package com.dmt.bankingapp.javaTests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.dmt.bankingapp.utils.DateAdjuster;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateAdjusterTest {

    @ParameterizedTest
    @CsvSource({
            "2023-06-25T10:15:30, 2023-06-25",
            "2021-01-01T00:00:00, 2021-01-01",
            "1999-12-31T23:59:59, 1999-12-31",
            "2000-02-29T12:34:56, 2000-02-29",
            "2022-12-25T18:45:30, 2022-12-25",
            "2010-04-05T05:06:07, 2010-04-05",
            "1980-07-15T14:22:30, 1980-07-15",
            "2025-11-11T11:11:11, 2025-11-11",
            "2030-08-22T21:00:00, 2030-08-22",
            "2015-03-03T03:03:03, 2015-03-03",
            "1975-09-10T09:10:11, 1975-09-10",
            "2001-01-01T01:01:01, 2001-01-01",
            "1990-05-20T20:20:20, 1990-05-20",
            "2012-12-12T12:12:12, 2012-12-12",
            "2024-07-04T04:04:04, 2024-07-04"
    })
    void testGetDate(String inputDateTime, String expectedDate) {
        LocalDateTime inputDate = LocalDateTime.parse(inputDateTime);
        String result = DateAdjuster.getDate(inputDate);
        assertEquals(expectedDate, result);
    }
}