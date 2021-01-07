package com.example.mobilki.classes;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {
    private String id;
    private String shop;
    private ArrayList<Item> items;
    private String address;
    private String city;
    private String userID;
    private String nameSurname;
    private String status;
    private String courierID;
    //private Location


    public ShoppingList(String id, String shop, ArrayList<Item> items, String address, String city, String userID, String nameSurname, String status, String courierID) {
        this.id = id;
        this.shop = shop;
        this.items = items;
        this.address = address;
        this.city = city;
        this.userID = userID;
        this.nameSurname = nameSurname;
        this.status = status;
        this.courierID = courierID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCourierID(String courierID) {
        this.courierID = courierID;
    }

    public String getStatus() {
        return status;
    }

    public String getCourierID() {
        return courierID;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public ShoppingList() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
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
