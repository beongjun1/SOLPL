package com.example.solpl1.post_management;

public class post_management_item {
    private String name;
    private String time;
    private String title;
    private String content;
    private int picture_id;

    public post_management_item(){}

    public post_management_item(String name, String time, String title, String content, int picture_id) {
        this.name = name;
        this.time = time;
        this.title = title;
        this.content = content;
        this.picture_id = picture_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(int picture_id) {
        this.picture_id = picture_id;
    }
}
