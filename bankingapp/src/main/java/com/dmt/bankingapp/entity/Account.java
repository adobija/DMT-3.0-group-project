package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    private Integer accountID;

    @Column(name = "accountNumber")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @Column(name = "accountBalance")
    private double accountBalance;

    @Enumerated(EnumType.STRING)
    @Column(name = "accountType")
    private AccountType accountType;

    @OneToOne
    @JoinColumn(name = "loanId", referencedColumnName = "loanId", nullable = true)
    private Loan loan;

    @OneToOne
    @JoinColumn(name = "depositId", referencedColumnName = "depositId", nullable = true)
    private Deposit deposit;

    @OneToOne(mappedBy = "checkingAccount", cascade = CascadeType.ALL)
    private Client checkingAccountClient;

    public Account(String accountNumber, AccountType accountType, Client client) {
        this.accountNumber = accountNumber;
        this.accountBalance = 0.0;
        this.accountType = accountType;
        this.client = client;
        if (client != null) {
            client.addAccount(this);
        }
    }

    public Account(String accountNumber, AccountType accountType, Client client, Loan loan) {
        this(accountNumber, accountType, client);
        if (accountType == AccountType.LOAN) {
            this.loan = loan;
        } else {
            throw new IllegalArgumentException("Loan can only be assigned for accounts of the LOAN type!");
        }
    }

    public Account(String accountNumber, AccountType accountType, Client client, Deposit deposit) {
        this(accountNumber, accountType, client);
        if (accountType == AccountType.DEPOSIT) {
            this.deposit = deposit;
        } else {
            throw new IllegalArgumentException("Deposit can only be assigned for accounts of the DEPOSIT type!");
        }
    }
    
    public Account() {}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double newAmountBalance, boolean isExpense) {
        if (isExpense) {
            this.accountBalance -= newAmountBalance;
        } else {
            this.accountBalance += newAmountBalance;
        }
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public enum AccountType {
        CHECKING,
        LOAN,
        DEPOSIT,
        BANK
    }

    public Loan getLoan() {
        return loan;
    }

    public Deposit getDeposit() {
        return deposit;
    }
    
    public void setLoan(Loan loan) {
        if (this.accountType == AccountType.LOAN) {
            this.loan = loan;
        } else {
            throw new IllegalArgumentException("Loan can only be assigned for accounts of the LOAN type!");
        }
    }

    public void setDeposit(Deposit deposit) {
        if (this.accountType == AccountType.DEPOSIT) {
            this.deposit = deposit;
        } else {
            throw new IllegalArgumentException("Deposits can only be assigned for accounts of the DEPOSIT type!");
        }
    }
}
