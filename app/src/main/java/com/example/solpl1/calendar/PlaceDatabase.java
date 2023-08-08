package com.example.solpl1.calendar;

public class PlaceDatabase {
    private String name;
    private String imageUrl; // 비트맵 대신 이미지 URL 저장
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

    public PlaceDatabase(String name, String imageUrl, String time) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.time = time;
    }
}
