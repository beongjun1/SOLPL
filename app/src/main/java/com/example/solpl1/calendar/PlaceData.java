package com.example.solpl1.calendar;

import android.graphics.Bitmap;

import java.io.Serializable;


public class PlaceData {
    private String name;
    private Bitmap imageBitmap; // 이미지를 비트맵 형태로 저장할 필드
    private String time;


    public PlaceData() {
    }

    public PlaceData(String name, Bitmap imageBitmap, String time) {
        this.name = name;
        this.imageBitmap = imageBitmap;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}