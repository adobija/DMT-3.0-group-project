package com.dmt.bankingapp.record.deposit;

import com.dmt.bankingapp.entity.Deposit;

import java.time.LocalDateTime;

public record DepositRecord(int depositID, String creationTime, int duration, double depositAmount, Deposit.DepositType depositType, String info) {

    @Override
    public int depositID() {
        return depositID;
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
    public double depositAmount() {
        return depositAmount;
    }

    @Override
    public Deposit.DepositType depositType() {
        return depositType;
    }

    @Override
    public String info() {
        return info;
    }
}
