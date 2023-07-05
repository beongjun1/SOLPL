package com.example.solpl1.calendar;

import android.graphics.Bitmap;

public class PlaceData {
    private String name;
    private Bitmap imageBitmap; // 이미지를 비트맵 형태로 저장할 필드

    public PlaceData(String name, Bitmap imageBitmap) {
        this.name = name;
        this.imageBitmap = imageBitmap;
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