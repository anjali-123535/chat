package com.example.chat.model;

public class Client {
    private String name,number,uid;

    public Client(String name, String number, String uid) {
        this.name = name;
        this.number = number;
        this.uid = uid;
    }
    public Client(){}
    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
