package com.dmt.bankingapp.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Deposits {

    private int depositID;
    private double depositAmount;
    private double interestRate;
    private boolean isCapitalized;

    public Deposits(double depositAmount, double interestRate, boolean isCapitalized) {
        this.depositAmount = depositAmount;
        this.interestRate = interestRate;
        this.isCapitalized = isCapitalized;
    }

    public static double calculateFixedTermDeposit(double depositAmount, double numberOfMonthsOnDeposit,
            double interestRate) {
        double interest = depositAmount * (numberOfMonthsOnDeposit / 12) * (interestRate / 100);
        return depositAmount + interest;
    }

    public static double calculateProgressiveDeposit(double depositAmount, int numberOfQuarters) {
        if (numberOfQuarters > 4) {
            throw new IllegalArgumentException("Number of quarters cannot exceed 4.");
        }

        double[] interestRates = { 0.02, 0.04, 0.06, 0.08 }; // Quarterly interest rates

        for (int i = 0; i < numberOfQuarters; i++) {
            double interest = depositAmount * interestRates[i];
            depositAmount += interest;
        }

        BigDecimal roundToTwoDecimalPlaces = new BigDecimal(Double.toString(depositAmount));
        roundToTwoDecimalPlaces = roundToTwoDecimalPlaces.setScale(2, RoundingMode.HALF_UP);
        return roundToTwoDecimalPlaces.doubleValue();
    }
    public static void main(String[] args) {

        double fixedTermDeposit = calculateFixedTermDeposit(1000, 12, 10);
        System.out.println("Fixed term: " + fixedTermDeposit);

        double progressiveDeposit = calculateProgressiveDeposit(1000, 4);
        System.out.println("Progressive: " + progressiveDeposit);
        
    }
}