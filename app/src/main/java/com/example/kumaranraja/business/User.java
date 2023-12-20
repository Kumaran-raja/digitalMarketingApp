package com.example.kumaranraja.business;

public class User {
    private String name;
    private String phone;
    private String email;
    private String dob;
    private String address; // Add this line if you want to include the address


    private String sponsor;
    private String profileID;
    private String RegDate;
    private String status;
    private  String plan;







  /*  public User(String plan) {
        this.plan = plan;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
*/

    // Empty constructor (required by Firebase)
    public User() {
    }

    public User(String name, String phone, String email, String dob, String address,String refer, String uniqueCode, String currentDate, String idStatus, String plan) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.sponsor = refer;
        this.profileID = uniqueCode;
        this.RegDate = currentDate;
        this.status = idStatus;
        this.plan = plan;


    }

    public User(String planstatuschange) {

    }


    // Getter and setter methods for each field

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

}