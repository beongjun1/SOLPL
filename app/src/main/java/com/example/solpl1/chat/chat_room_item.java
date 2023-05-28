package com.example.solpl1.chat;

public class chat_room_item {
    private String name;
    private String message;
    private String message_count;
    private String time;
    private int pictureResId;

    public chat_room_item(String name, String message, String message_count, String time, int pictureResId) {
        this.name = name;
        this.message = message;
        this.message_count = message_count;
        this.time = time;
        this.pictureResId = pictureResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_count() {
        return message_count;
    }

    public void setMessage_count(String message_count) {
        this.message_count = message_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPictureResId() {
        return pictureResId;
    }

    public void setPictureResId(int pictureResId) {
        this.pictureResId = pictureResId;
    }

    public chat_room_item(){

    }
}
