package com.example.mobilki.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {
    private final String id;
    private String shop;
    private ArrayList<Item> items;
    private String address;
    private String city;
    //private String userID;
    //private Location

    public ShoppingList(String id, String shop, ArrayList<Item> items, String address, String city) {
        this.id = id;
        this.shop = shop;
        this.items = items;
        this.address = address;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getShop() {
        return shop;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
