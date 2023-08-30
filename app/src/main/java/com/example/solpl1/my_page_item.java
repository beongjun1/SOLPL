package com.example.solpl1;

public class my_page_item {
    String date;
    String loc;
    String img;

    public my_page_item(String date, String loc, String img) {
        this.date = date;
        this.loc = loc;
        this.img = img;
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