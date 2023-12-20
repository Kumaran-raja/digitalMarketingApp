package com.example.kumaranraja.business;

public class UserProfile {
    String sponserID;
    public UserProfile(){

    }

    public UserProfile(String sponserID) {
        this.sponserID = sponserID;
    }

    public String getSponserID() {
        return sponserID;
    }

    public void setSponserID(String sponserID) {
        this.sponserID = sponserID;
    }
}
