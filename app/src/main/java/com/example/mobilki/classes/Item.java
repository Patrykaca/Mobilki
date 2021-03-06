package com.example.mobilki.classes;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private float amount;
    private String measurement;

    public Item(String name, float amount, String measurement) {
        this.name = name;
        this.amount = amount;
        this.measurement = measurement;
    }

    public Item() {
    }


    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
