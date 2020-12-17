package com.example.mobilki;

public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String age;
    public String password;

    public User() {
    }

    public User(String firstName, String lastName, String email, String age, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.password = password;
    }
}
