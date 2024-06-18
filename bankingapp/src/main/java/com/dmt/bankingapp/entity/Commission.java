package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

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
    private int commissionRateInPercent;

    public Commission(int commissionRateInPercent, String commissionOf) {
        this.commissionRateInPercent = commissionRateInPercent;
        this.commissionOf = commissionOf;
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

    public int getCommissionRateInPercent() {
        return commissionRateInPercent;
    }

    public void setCommissionRateInPercent(int commissionRateInPercent) {
        this.commissionRateInPercent = commissionRateInPercent;
    }
}
