package com.dmt.bankingapp.record.transactions;

public record TransactionRecord(int transactionID, String timestamp, double amount, String giverAccountNumber, String receiverAccountNumber) {
    @Override
    public int transactionID() {
        return transactionID;
    }

    @Override
    public String timestamp() {
        return timestamp;
    }

    @Override
    public double amount() {
        return amount;
    }

    @Override
    public String giverAccountNumber() {
        return giverAccountNumber;
    }

    @Override
    public String receiverAccountNumber() {
        return receiverAccountNumber;
    }
}
