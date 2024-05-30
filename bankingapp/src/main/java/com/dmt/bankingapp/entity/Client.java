package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientID")
    private int clientID;

    @Column(name = "clientName")
    private String clientName;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "bcryptClientPassword", length = 68)
    private String clientPassword;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Loan> loans = new ArrayList<>();

    public Client(String clientName, boolean isAdmin, String clientPassword) {
        this.clientName = clientName;
        this.isAdmin = isAdmin;
        this.clientPassword = createBcryptHashedPassword(clientPassword);
    }

    public Client() {}

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = createBcryptHashedPassword(clientPassword);
    }

    public List<Account> getAccountsList() {
        return accounts;
    }

    public void setAccountsList(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }

    public List<Loan> getLoansList() {
        return loans;
    }

    public void setLoansList(List<Loan> loans) {
        this.loans = loans;
    }

    public void addLoan(Loan loan) {
        loan.setClient(this);
        this.loans.add(loan);
    }

    // Method to create bcrypted password from plain text to insert into database crypted password
    private String createBcryptHashedPassword(String plainTextPassword) {
        int numberOfRounds = 10;
        String hashingSalt = BCrypt.gensalt(numberOfRounds);
        return "{bcrypt}" + BCrypt.hashpw(plainTextPassword, hashingSalt);
    }
}
