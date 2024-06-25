package com.dmt.bankingapp.record.installments;

public record LoanInstallment(int installmentID, double installmentAmount, boolean isPaid, double paid, double toPay, String dueDate) {
    @Override
    public int installmentID() {
        return installmentID;
    }

    @Override
    public double installmentAmount() {
        return installmentAmount;
    }

    @Override
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public double paid() {
        return paid;
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
