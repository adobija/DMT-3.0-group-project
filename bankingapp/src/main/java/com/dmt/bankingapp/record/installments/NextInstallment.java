package com.dmt.bankingapp.record.installments;

public record NextInstallment(String loanAccNumber, int installmentID, double installmentAmount, double paidAmount, double toPay, String dueDate) {
    @Override
    public String loanAccNumber() {
        return loanAccNumber;
    }

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
