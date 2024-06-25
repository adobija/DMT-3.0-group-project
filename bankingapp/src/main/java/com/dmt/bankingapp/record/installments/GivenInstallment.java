package com.dmt.bankingapp.record.installments;

public record GivenInstallment(int installmentID, int loanID, int clientID, boolean isPaid, double installmentAmount, double paidAmount, double toPay, String dueDate) {
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

    @Override
    public double paidAmount() {
        return paidAmount;
    }

    @Override
    public double toPay() {
        return toPay;
    }

    @Override
    public String dueDate() {
        return dueDate;
    }
}
