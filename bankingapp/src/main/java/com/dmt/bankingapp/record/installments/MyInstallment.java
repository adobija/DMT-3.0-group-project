package com.dmt.bankingapp.record.installments;

public record MyInstallment(String loanAccNumber, int loanID, int installmentID, double installmentAmount, double paidAmount, boolean isPaid, String dueDate) {
    @Override
    public int loanID() {
        return loanID;
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
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public String dueDate() {
        return dueDate;
    }
}
