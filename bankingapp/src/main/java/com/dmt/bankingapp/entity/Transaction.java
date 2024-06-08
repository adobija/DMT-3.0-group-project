package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.dmt.bankingapp.entity.Account.AccountType;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionID")
    private int transactionID;
    
    @ManyToOne
    @JoinColumn(name = "accountOfSender", referencedColumnName = "accountID")
    private Account giver;

    @ManyToOne
    @JoinColumn(name = "accountOfReceiver", referencedColumnName = "accountID")
    private Account receiver;
    private double amount;

    @Column(name = "dateOfTransaction")
    private LocalDateTime timestamp;

    public Transaction(Account giver, Account receiver, double amount) {
        this.giver = giver;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();

        // Checking the account balance to avoid the balance falling below 0
        if (this.amount > giver.getAccountBalance()) {
                throw new IllegalStateException("You cannot transfer more money than you have on the account!");
        }

        // If giver is a loan account evaluating the account balance, to allow only for one outgoing transaction from this account /first transaction when loan is granted/
        if (giver.getAccountType().equals(AccountType.LOAN)) {
            if (giver.getAccountBalance() != 0) {
                throw new IllegalStateException("You cannot transfer money from the loan account!");
            }
        }
    
        // If receiver is a loan account check whether the loan is still active and handle unpaid installments
        if (receiver.getAccountType().equals(AccountType.LOAN)) {
            Loan loan = receiver.getLoan();
            if (loan.getIsActive()) {
                double amountLeft = this.amount;
                double amountUsedForPayments = 0;
                List<Installment> loanInstallments = loan.getInstallments();
    
                // Filter unpaid installments of the loan
                List<Installment> unpaidInstallments = loanInstallments.stream()
                    .filter(installment -> !installment.getIsPaid())
                    .sorted(Comparator.comparing(Installment::getDueDate))
                    .collect(Collectors.toList());
    
                for (Installment installment : unpaidInstallments) {
                    if (amountLeft <= 0) {
                        break;
                    }
    
                    double dueAmount = installment.getInstallmentAmount() - installment.getPaidAmount();
                    double payment = Math.min(amountLeft, dueAmount); // Ensure not paying more than the provided amount
                    installment.setPaidAmount(installment.getPaidAmount() + payment);
                    amountLeft -= payment;
                    amountUsedForPayments += payment;
    
                    if (installment.getPaidAmount() == installment.getInstallmentAmount()) {
                        installment.setIsPaid(true);
                    }
                }
    
                // Transfer only the amount used for payments, remaining amount stays with the giver
                manipulateTransaction(giver, receiver, amountUsedForPayments);
    
                if (unpaidInstallments.isEmpty() || amountLeft == 0) {
                    // Setting loan as paid - inactive if fully paid
                    loan.setIsActive(false);
                }
            } else {
                // Handle the case where the loan is already paid
                throw new IllegalStateException("You cannot transfer money to the loan account since the loan has already been paid!");
            }
        } else {
            // If the receiver account is not loan, money are transfered without any other operations
            manipulateTransaction(giver, receiver, amount);
        }
    }
    
    public Transaction() {
    }

    public Account getGiver() {
        return giver;
    }

    public void setGiver(Account giver) {
        this.giver = giver;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void manipulateTransaction(Account giver, Account receiver, double amount){
        giver.setAccountBalance(amount, true);
        receiver.setAccountBalance(amount, false);
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
