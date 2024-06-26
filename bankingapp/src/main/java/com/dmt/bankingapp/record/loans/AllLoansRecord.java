package com.dmt.bankingapp.record.loans;

public record AllLoansRecord(int loanID, String date, String loanAccountNumber, double totalAmount, double leftToPay, int clientID) {
    @Override
    public int loanID() {
        return loanID;
    }

    @Override
    public String date() {
        return date;
    }

    @Override
    public String loanAccountNumber() {
        return loanAccountNumber;
    }

    @Override
    public double totalAmount() {
        return totalAmount;
    }

    @Override
    public double leftToPay() {
        return leftToPay;
    }

    @Override
    public int clientID() {
        return clientID;
    }
}
