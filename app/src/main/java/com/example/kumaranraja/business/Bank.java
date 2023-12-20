package com.example.kumaranraja.business;

public class Bank {
    private String accountnumber;
    private String confirmaccountno;
    private String accountholdername;
    private String bankIFSCcode;
    private String bank;
    private String branch;
    public Bank() {
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getConfirmaccountno() {
        return confirmaccountno;
    }

    public void setConfirmaccountno(String confirmaccountno) {
        this.confirmaccountno = confirmaccountno;
    }

    public String getAccountholdername() {
        return accountholdername;
    }

    public void setAccountholdername(String accountholdername) {
        this.accountholdername = accountholdername;
    }

    public String getBankIFSCcode() {
        return bankIFSCcode;
    }

    public void setBankIFSCcode(String bankIFSCcode) {
        this.bankIFSCcode = bankIFSCcode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Bank(String accountnumber, String confirmaccountno, String accountholdername, String bankIFSCcode, String bank, String branch) {
        this.accountnumber = accountnumber;
        this.confirmaccountno = confirmaccountno;
        this.accountholdername = accountholdername;
        this.bankIFSCcode = bankIFSCcode;
        this.bank = bank;
        this.branch = branch;

    }
}
