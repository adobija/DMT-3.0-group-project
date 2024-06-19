package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Commissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commissionId")
    private Integer commisionId;

    @Column(name = "commissionOf")
    private String commissionOf;

    @Column(name = "commissionRateInPercent")
    private double commissionRateInPercent;

    @Column(name = "creationTime")
    private LocalDateTime creationTime;

    public Commission(double commissionRateInPercent, String commissionOf, LocalDateTime creationTime) {
        this.commissionRateInPercent = commissionRateInPercent;
        this.commissionOf = commissionOf;
        this.creationTime = creationTime;
    }

    public Commission() {
    }

    public Integer getCommisionId() {
        return commisionId;
    }

    public String getCommissionOf() {
        return commissionOf;
    }

    public void setCommissionOf(String commissionOf) {
        this.commissionOf = commissionOf;
    }

    public double getCommissionRateInPercent() {
        return commissionRateInPercent;
    }

    public void setCommissionRateInPercent(double commissionRateInPercent) {
        this.commissionRateInPercent = commissionRateInPercent;
    }

    public LocalDateTime getcreationTime() {
        return creationTime;
    }
}
