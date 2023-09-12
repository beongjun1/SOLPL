package com.example.solpl1.calendar;

import android.graphics.Bitmap;

import java.util.List;

public class calendar_item {
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

    public calendar_item(String name, String imageUrl, String time, String date) {
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
//    private String date;
//    private List<Place> placeList;
//
//    public calendar_item(String date, List<Place> placeList) {
//        this.date = date;
//        this.placeList = placeList;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public List<Place> getPlaceList() {
//        return placeList;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public void setPlaceList(List<Place> placeList) {
//        this.placeList = placeList;
//    }
}