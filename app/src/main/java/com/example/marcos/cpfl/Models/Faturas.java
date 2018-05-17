package com.example.marcos.cpfl.Models;

public class Faturas {

    private int year, month;
    private float consume;

    public Faturas(int month, int year, float consume) {
        this.month = month;
        this.year = year;
        this.consume = consume;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public float getConsume() {
        return consume;
    }

}
