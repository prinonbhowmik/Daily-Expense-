package com.my.dailyexpense;

public class GetExpense {

    private String exp_type;
    private int amount;
    private String time;
    private String date;
    private int id;
    private String image;

    public GetExpense() {
    }

    public GetExpense(String exp_type, int amount,String time, String date,int id,String image) {
        this.exp_type = exp_type;
        this.amount = amount;
        this.time = time;
        this.date = date;
        this.id = id;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getExp_type() {
        return exp_type;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
