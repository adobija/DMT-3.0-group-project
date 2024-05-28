package com.dmt.bankingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID")
    private int userID;
    private String userName;
    private boolean isAdmin;

    //length 68 because {bcrypt}+hash has in total 68 characters
    @Column(name = "bcrypt_userPassword", length = 68)
    private String userPassword;

    public User(String userName, boolean isAdmin, String userPassword) {
        this.userName = userName;
        this.isAdmin = isAdmin;
        this.userPassword = userPassword;
    }
    public User(){
    }

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
        this.userPassword = userPassword;
    }
}