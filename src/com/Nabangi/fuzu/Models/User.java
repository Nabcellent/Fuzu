package com.Nabangi.fuzu.Models;

/**
 * _____________________________________________________    Nabangi Michael - ICS B - 134694, 21/03/2021    _________✔*/


public class User {
    int id;
    String lastName;

    public User(int id, String last_name) {
        this.id = id;
        this.lastName = last_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }
}
