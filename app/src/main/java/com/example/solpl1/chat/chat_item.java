package com.example.solpl1.chat;

public class chat_item {
    private String title;
    private String name;
    private String email;
    private String content;
    private String time;
    private int pictureResId;

    private String memberCountCurrent;
    private String memberCountTotal;


    public chat_item(String title, String name, String email, String content, String time, int pictureResId, String memberCountCurrent, String memberCountTotal) {
        this.title = title;
        this.name = name;
        this.email = email;
        this.content = content;
        this.time = time;
        this.pictureResId = pictureResId;
        this.memberCountCurrent = memberCountCurrent;
        this.memberCountTotal = memberCountTotal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getMemberCountCurrent() {
        return memberCountCurrent;
    }

    public void setMemberCountCurrent(String memberCountCurrent) {
        this.memberCountCurrent = memberCountCurrent;
    }

    public String getMemberCountTotal() {
        return memberCountTotal;
    }

    public void setMemberCountTotal(String memberCountTotal) {
        this.memberCountTotal = memberCountTotal;
    }

    public chat_item() {

    }

}
