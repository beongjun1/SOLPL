package com.example.solpl1.calendar;

import android.graphics.Bitmap;

import java.util.List;

public class calendar_item {
//    private String calendarId;
    private String name;
    private String imageUrl; // 이미지를 비트맵 형태로 저장할 필드
    private String time;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public calendar_item( String name, String imageUrl, String time) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.time = time;
    }
}
