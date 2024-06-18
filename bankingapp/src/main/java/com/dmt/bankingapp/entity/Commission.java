package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Comissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comissionId")
    private Integer commisionId;

    @Column(name = "comissionOf")
    private String comissionOf;

    @Column(name = "commissionRateInPercent")
    private int commissionRateInPercent;

    public Commission(int commissionRateInPercent, String comissionOf) {
        this.commissionRateInPercent = commissionRateInPercent;
        this.comissionOf = comissionOf;
    }

    public Integer getCommisionId() {
        return commisionId;
    }

    public String getCommissionOf() {
        return comissionOf;
    }

    public void setCommissionOf(String comissionOf) {
        this.comissionOf = comissionOf;
    }

    public int getCommissionRateInPercent() {
        return commissionRateInPercent;
    }

    public void setCommissionRateInPercent(int commissionRateInPercent) {
        this.commissionRateInPercent = commissionRateInPercent;
    }
}
