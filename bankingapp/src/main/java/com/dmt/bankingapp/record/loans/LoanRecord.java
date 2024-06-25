package com.dmt.bankingapp.record.loans;

public record LoanRecord(int loanID, String formattedDate, String accountNumber, double totalLoanAmount, double leftToPay, int clientID, int loanDuration, double interestRate, double commissionRate, boolean isActive) {
    @Override
    public int loanID() {
        return loanID;
    }

    @Override
    public String formattedDate() {
        return formattedDate;
    }

    @Override
    public String accountNumber() {
        return accountNumber;
    }

    @Override
    public double totalLoanAmount() {
        return totalLoanAmount;
    }

    @Override
    public double leftToPay() {
        return leftToPay;
    }

    @Override
    public int clientID() {
        return clientID;
    }

    @Override
    public int loanDuration() {
        return loanDuration;
    }

    @Override
    public double interestRate() {
        return interestRate;
    }

    @Override
    public double commissionRate() {
        return commissionRate;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }
}
