package com.dmt.bankingapp.record.loans;

import java.time.LocalDateTime;

public record ClientLoan(int LoanID, LocalDateTime creationTime, int duration, double leftToPay) {
    @Override
    public int LoanID() {
        return LoanID;
    }

    @Override
    public LocalDateTime creationTime() {
        return creationTime;
    }

    @Override
    public int duration() {
        return duration;
    }

    @Override
    public double leftToPay() {
        return leftToPay;
    }
}
