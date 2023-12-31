package com.example.kumaranraja.business;

public class Payout {
    private int sno;
    private String taskDate;
    private String amountFrom;
    private int amount;
    private long timestamp;


    public Payout(int sno, String taskDate, String amountFrom, int amount, long timestamp) {
        this.sno = sno;
        this.taskDate = taskDate;
        this.amountFrom = amountFrom;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(String amountFrom) {
        this.amountFrom = amountFrom;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
// Getters and setters...
}