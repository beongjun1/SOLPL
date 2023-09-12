package com.example.solpl1.mypage;

public class my_page_calendar_item {
    private String name;
    private String imageUrl;
    private String time;
    private String date;

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public my_page_calendar_item(String name, String imageUrl, String time, String date) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTime() {
        return time;
    }
}
