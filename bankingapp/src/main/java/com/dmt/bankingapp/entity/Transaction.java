package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.dmt.bankingapp.entity.Account.AccountType;
import com.dmt.bankingapp.utils.DecimalPlacesAdjuster;

@Entity
@Table(name = "Transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private Integer transactionID;
    
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
        this.amount = DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(amount);
        this.timestamp = LocalDateTime.now();

        // Restricting transfers for negative amounts
        if (this.amount < 0) {
            throw new IllegalStateException("You cannot transfer negative amount!");
        }

        // Restricting transfers for amounts greater than 1 billion
        if (this.amount > 1000000000) {
            throw new IllegalStateException("You cannot transfer more than 1 billion!");
        }

        // Restricting making transfers from loan accounts ...
        if (giver.getAccountType().equals(AccountType.LOAN)) {
            // ... after loan is launched - only transfers to a bank account are allowed
            if (giver.getAccountBalance() < 0 && !receiver.getAccountType().equals(AccountType.BANK)) {
                throw new IllegalStateException("You cannot transfer from the loan account!");
            }
            // ... after loan is redeemed
            if (giver.getAccountBalance() == 0 && giver.getLoan() != null) {
                throw new IllegalStateException("You cannot transfer from the loan account!");
            }            
        }

        // Checking the account balance for checking and saving accounts to avoid the balance falling below 0
        if (giver.getAccountType().equals(AccountType.CHECKING) || giver.getAccountType().equals(AccountType.DEPOSIT)) {
            if (this.amount > giver.getAccountBalance()) {
                    throw new IllegalStateException("You cannot transfer more money than you have on the account!");
            }
        }
        
        // If receiver is a loan account, handle loan payments
        if (receiver.getAccountType().equals(AccountType.LOAN)) {
            processLoanPayments(giver, receiver, amount);
        } else {
            // If the receiver account is not loan, money are transferred without any other operations
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
        this.amount = DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(amount);
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void manipulateTransaction(Account giver, Account receiver, double amount){
        giver.setAccountBalance(DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(amount), true);
        receiver.setAccountBalance(DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(amount), false);
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

    public void processLoanPayments(Account giver, Account receiver, double amount) {
        Loan loan = receiver.getLoan();
        if (loan.getIsActive()) {
            double amountLeft = DecimalPlacesAdjuster.adjustToTwoDecimalPlaces(amount);
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

            // Update the amount in the transaction to reflect only the amount used for payments
            this.amount = amountUsedForPayments;

            // Updating the leftToPay amount in the loan
            loan.setLeftToPay(loan.getLeftToPay() - amountUsedForPayments);
            // If leftToPay is very small, set it to zero - handling  floating-point precision errors
            if (loan.getLeftToPay() < 0.01) {
                loan.setLeftToPay(0.0);
            }
    
            // After processing all payments checking if all installments has been paid
            boolean allInstallmentsPaid = loanInstallments.stream()
                .allMatch(Installment::getIsPaid);
    
            if (allInstallmentsPaid) {
                // Setting loan as paid - inactive if fully paid
                loan.setIsActive(false);
            }
        } else {
            // Handle the case where the loan is already paid
            throw new IllegalStateException("You cannot transfer money to this loan account since the loan has already been paid!");
        }
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", giver=" + giver +
                ", receiver=" + receiver +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}