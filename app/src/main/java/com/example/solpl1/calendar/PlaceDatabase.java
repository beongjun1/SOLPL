package com.example.solpl1.calendar;

public class PlaceDatabase {
    private String name;
    private String imageUrl; // 비트맵 대신 이미지 URL 저장
    private String time;
    private String place_id;
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public PlaceDatabase(String name, String imageUrl, String time, String place_id, String user_id) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.time = time;
        this.place_id = place_id;
        this.user_id = user_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

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

}
