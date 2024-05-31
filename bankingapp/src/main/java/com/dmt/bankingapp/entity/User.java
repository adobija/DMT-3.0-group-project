package com.dmt.bankingapp.entity;

import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID")
    private int userID;

    @Column(name = "userName")
    private String userName;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "bcrypt_userPassword", length = 68)
    private String userPassword;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private List<Loan> loans = new ArrayList<>();

    public User(String userName, boolean isAdmin, String userPassword) {
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.userPassword = createBcryptHashedPassword(userPassword);
    }

    public User() {}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = createBcryptHashedPassword(userPassword);
    }

    public List<Account> getAccountsList() {
        return accounts;
    }

    public void setAccountsList(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        account.setUser(this);
        this.accounts.add(account);
    }

    public List<Loan> getLoansList() {
        return loans;
    }

    public void setLoansList(List<Loan> loans) {
        this.loans = loans;
    }

    public void addLoan(Loan loan) {
        loan.setUser(this);
        this.loans.add(loan);
    }

    private List<Deposits> Deposits = new ArrayList<>();

    public List<Deposits> getDepositsList() {
        return Deposits;
    }

    public void setDepositsList(List<Deposits> deposits) {
        this.Deposits = deposits;
    }

    public void addDeposit(Deposits deposit) {
        deposit.setUser(this);
        this.Deposits.add(deposit);
    }

    // Method to create bcrypted password from plain text to insert into database crypted password
    private String createBcryptHashedPassword(String plainTextPassword) {
        int numberOfRounds = 10;
        String hashingSalt = BCrypt.gensalt(numberOfRounds);
        return "{bcrypt}" + BCrypt.hashpw(plainTextPassword, hashingSalt);
    }
}
