package com.example.chat.model;

public class FriendlyMessage {
    private String text;
    private String name,time;
    private String photoUrl,mobile,date;

    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name,String mobile,String photoUrl,String date,String time) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.mobile=mobile;
        this.date=date;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
