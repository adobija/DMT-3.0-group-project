package com.dmt.bankingapp.record.installments;

public record AllInstallment(String loanAccNumber, int installmentID, int loanID, int clientID, boolean isPaid, double installmentAmount) {
    @Override
    public int installmentID() {
        return installmentID;
    }

    @Override
    public int loanID() {
        return loanID;
    }

    @Override
    public int clientID() {
        return clientID;
    }

    @Override
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public double installmentAmount() {
        return installmentAmount;
    }
}
