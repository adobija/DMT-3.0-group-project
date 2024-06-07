package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Installments") // Use plural form for table names to be consistent
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "installmentID")
    private int installmentID;

    @ManyToOne
    @JoinColumn(name = "loanID", referencedColumnName = "loanID")
    private Loan loan;

    @Column(name = "installmentAmount")
    private double installmentAmount;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    public Installment() {
    }

    public Installment(Loan loan, double installmentAmount, LocalDateTime dueDate) {
        this.loan = loan;
        this.installmentAmount = installmentAmount;
        this.dueDate = dueDate;
    }

    public int getInstallmentID() {
        return installmentID;
    }

    public void setInstallmentID(int installmentID) {
        this.installmentID = installmentID;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public double getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(double installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
