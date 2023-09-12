package com.example.solpl1;

public class my_page_item {
    String date;
    String loc;
    String img;
    String key;

    public my_page_item(String date, String loc, String img, String key) {
        this.date = date;
        this.loc = loc;
        this.img = img;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}