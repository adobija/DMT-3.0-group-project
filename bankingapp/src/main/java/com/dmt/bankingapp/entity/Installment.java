package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Installment")
public class Installment {

    @ManyToOne
    @JoinColumn(name = "loan", referencedColumnName = "loanID")
    private Loan loan;
}
