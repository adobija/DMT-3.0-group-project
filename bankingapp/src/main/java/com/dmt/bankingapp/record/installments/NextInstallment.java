package com.dmt.bankingapp.record.installments;

public record NextInstallment(int installmentID, double installmentAmount, double paidAmount, double toPay, String dueDate) {
    @Override
    public int installmentID() {
        return installmentID;
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
