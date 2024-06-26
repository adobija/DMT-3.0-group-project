package com.dmt.bankingapp.record.loans;

import java.time.LocalDateTime;

public record ClientLoan(String loanAccountNumber, int LoanID, String creationTime, int duration, double leftToPay) {
    @Override
    public String loanAccountNumber() {
        return loanAccountNumber;
    }

    @Override
    public int LoanID() {
        return LoanID;
    }

    @Override
    public String creationTime() {
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
